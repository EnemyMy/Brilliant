package com.example.app_37_brilliantapp.finddiamond

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.data.NoSuchDocumentException
import com.example.app_37_brilliantapp.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FindTheDiamondViewModel (private val repository: FindDiamondRepository): ViewModel() {

    //two-way databinding
    val diamondName = MutableLiveData<String>()

    //two-way databinding
    val polishTime = MutableLiveData<String>()

    private val _saveDiamondEvent = MutableLiveData<Event<Unit>>()
    val saveDiamondEvent: LiveData<Event<Unit>> = _saveDiamondEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    fun setPolishTime(view: View) {
        (view as? EditText)?.showDateTimePickerDialog()
    }

    fun startSaveDiamondEvent() {
        val name = diamondName.value ?: ""
        val time = polishTime.value ?: ""
        if (!validateFields(name, time)) {
            showSnackbar(SnackbarEvent("Fields validation failed"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getCurrentDiamond()) {
                is Result.Success -> {
                    showSnackbar(SnackbarEvent("You already have diamond. Do you want to abandon it?", Snackbar.LENGTH_LONG, "Abandon") {
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.saveCurrentDiamond(CurrentDiamond(name, Calendar.getInstance().toFormattedString(), time))
                            showSnackbar(SnackbarEvent("Polishing started!"))
                            notifySaveDiamondEventEnd()
                        }
                    })
                }
                is Result.Error -> {
                    when (result.exception) {
                        is NoSuchDocumentException -> {
                            repository.saveCurrentDiamond(CurrentDiamond(name, Calendar.getInstance().toFormattedString(), time))
                            showSnackbar(SnackbarEvent("Polishing started!"))
                            notifySaveDiamondEventEnd()
                        }
                        is InvalidEmailException -> {
                            showSnackbar(SnackbarEvent("Error while handling email. Check your login data"))
                        }
                        else -> {
                            Log.e("Saving failed", "${result.exception}")
                            showSnackbar(SnackbarEvent("Saving failed"))
                        }
                    }
                }
            }
        }
    }

    fun showSnackbar(event: SnackbarEvent) = viewModelScope.launch(Dispatchers.Main) { _snackBarEvent.value = event }

    private fun notifySaveDiamondEventEnd() = viewModelScope.launch(Dispatchers.Main) { _saveDiamondEvent.value = Event(Unit) }

    private fun validateFields(name: String, time: String): Boolean = name != "" && time != ""
}