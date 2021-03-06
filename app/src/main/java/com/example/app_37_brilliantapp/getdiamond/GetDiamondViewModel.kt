package com.example.app_37_brilliantapp.getdiamond

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.Difficulty
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.example.app_37_brilliantapp.util.differenceToGivenInTime
import com.example.app_37_brilliantapp.util.toDateOrToday
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class GetDiamondViewModel (private val repository: GetDiamondRepository): ViewModel() {

    private val userEmail = MutableLiveData<String?>().apply {
        FirebaseAuth.getInstance().addAuthStateListener {
            Log.e("FirebaseAuth", "AuthStateChanged: currentUser: ${it.currentUser}")
            this.value = it.currentUser?.email
        }
    }

    private val _getDiamondEvent = MutableLiveData<Unit>()
    val getDiamondEvent: LiveData<Unit> = _getDiamondEvent

    private val _snackbarEvent = MutableLiveData<Event<SnackbarEvent>>()
    val snackbarEvent: LiveData<Event<SnackbarEvent>> = _snackbarEvent

    //two way databinding
    val rating = MutableLiveData<Float>()

    private val _diamondSuccessfullySavedEvent = MutableLiveData<Event<Unit>>()
    val diamondSuccessfullySavedEvent: LiveData<Event<Unit>> = _diamondSuccessfullySavedEvent

    fun startGetDiamondEvent() {
        if (userEmail.value != null || userEmail.value != "") {
            viewModelScope.launch(Dispatchers.Main) {
                val diamondResult = repository.getCurrentDiamond()
                if (diamondResult.succeeded) {
                    val diamond = (diamondResult as Result.Success).data
                    val time = getTime(diamond.start.toDateOrToday().time)
                    repository.addEarnedDiamond(EarnedDiamond(diamond.title, diamond.deadline, time, convertRatingToDifficulty()))
                    repository.deleteCurrentDiamond()
                    showSnackbar(SnackbarEvent("You successfully earned your diamond!"))
                    startDiamondSuccessfullySavedEvent()
                }
                else {
                    showSnackbar(SnackbarEvent("Error while trying to get diamond", Snackbar.LENGTH_LONG, "Try again") {
                        startGetDiamondEvent()
                    })
                }
            }
        }
        else
            showSnackbar(SnackbarEvent("Error while trying to get diamond", Snackbar.LENGTH_LONG, "Try again") {
                startGetDiamondEvent()
            })
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackbarEvent.value = Event(event)
    }

    private fun startDiamondSuccessfullySavedEvent() {
        _diamondSuccessfullySavedEvent.value = Event(Unit)
    }

    private fun getTime(start: Long): String {
        return Date().time.differenceToGivenInTime(start)
    }

    private fun convertRatingToDifficulty(): Difficulty {
        return when(rating.value?.roundToInt()) {
            2 -> Difficulty.EASY
            3 -> Difficulty.MEDIUM
            4 -> Difficulty.HARD
            5 -> Difficulty.CHALLENGING
            else -> Difficulty.ELEMENTARY
        }
    }
}