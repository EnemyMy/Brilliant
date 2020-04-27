package com.example.app_37_brilliantapp.finddiamond

import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.Repository
import com.example.app_37_brilliantapp.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FindTheDiamondViewModel (private val repository: Repository): ViewModel() {

    //two-way databinding
    val diamondName = MutableLiveData<String>()

    //two-way databinding
    val polishTime = MutableLiveData<String>()

    private val _saveDiamondEvent = MutableLiveData<Unit>()
    val saveDiamondEvent: LiveData<Unit> = _saveDiamondEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    fun setPolishTime(view: View) {
        (view as? EditText)!!.showDateTimePickerDialog()
    }

    fun startSaveDiamondEvent() {
        if (!validateFields()) {
            showSnackbar(SnackbarEvent("Fields validation failed"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getCurrentDiamond()) {
                is Result.Success -> {
                    showSnackbar(SnackbarEvent("You already have diamond. Do you want to abandon it?", Snackbar.LENGTH_LONG, "Abandon") {
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.saveCurrentDiamond(CurrentDiamond(diamondName.value!!, Calendar.getInstance().toFormattedString(), polishTime.value!!))
                            showSnackbar(SnackbarEvent("Polishing started!"))
                            notifySaveDiamondEventEnd()
                        }
                    })
                }
                is Result.Error -> {
                    if (result.noSuchDocument) {
                        repository.saveCurrentDiamond(CurrentDiamond(diamondName.value!!, Calendar.getInstance().toFormattedString(), polishTime.value!!))
                        showSnackbar(SnackbarEvent("Polishing started!"))
                        notifySaveDiamondEventEnd()
                    }
                    else
                        showSnackbar(SnackbarEvent("Saving failed"))
                }
            }
        }
    }

    fun showSnackbar(event: SnackbarEvent) = viewModelScope.launch(Dispatchers.Main) { _snackBarEvent.value = event }

    private fun notifySaveDiamondEventEnd() = viewModelScope.launch(Dispatchers.Main) { _saveDiamondEvent.value = Unit }

    private fun validateFields(): Boolean = diamondName.value != null && diamondName.value != "" && polishTime.value != null && polishTime.value != ""
}