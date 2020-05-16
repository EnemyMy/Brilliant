package com.example.app_37_brilliantapp.currentdiamond

import android.util.Log
import androidx.lifecycle.*
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentDiamondViewModel (private val repository: CurrentDiamondRepository): ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    //TODO("Replace with liveData{}")
    private val userEmail = MutableLiveData<String?>().apply {
        FirebaseAuth.getInstance().addAuthStateListener {
            Log.e("FirebaseAuth", "AuthStateChanged: currentUser: ${it.currentUser}")
            this.value = it.currentUser?.email
        }
    }

    private val _logoutEvent = MutableLiveData<Event<Unit>>()
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val _findTheDiamondEvent = MutableLiveData<Event<Unit>>()
    val findTheDiamondEvent: LiveData<Event<Unit>> = _findTheDiamondEvent

    private val _getDiamondEvent = MutableLiveData<Event<Unit>>()
    val getDiamondEvent: LiveData<Event<Unit>> = _getDiamondEvent

    private val _cancelDiamondEvent = MutableLiveData<Event<Unit>>()
    val cancelDiamondEvent: LiveData<Event<Unit>> = _cancelDiamondEvent

    val currentDiamond: LiveData<CurrentDiamond> = userEmail.switchMap {email ->
        Log.e("MLD currentDiamond", "Start observing")
        if (email != null && email != "")
            repository.observeCurrentDiamond().distinctUntilChanged().switchMap { handleResult(it) }
        else MutableLiveData<CurrentDiamond>(null)
    }

    val noDiamond: LiveData<Boolean> = currentDiamond.map {
        Log.e("LD noDiamond", "Value: ${currentDiamond.value == null} CurrentDiamond title: ${currentDiamond.value?.title}")
        currentDiamond.value == null
    }

    val diamondName: LiveData<String> = noDiamond.map {state ->
        if (!state) {
            Log.e("LD diamondName", "Value: ${currentDiamond.value?.title}")
            currentDiamond.value?.title ?: ""
        }
        else {
            Log.e("LD diamondName", "Value: ${""}")
            "No name"
        }
    }

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    private fun handleResult(data: Result<CurrentDiamond>): LiveData<CurrentDiamond> {
        val result = MutableLiveData<CurrentDiamond>(null)
        when(data) {
            is Result.Success -> result.value = data.data
            is Result.Error -> {
                if (data.exception !is InvalidEmailException)
                    showSnackbar(SnackbarEvent("Error while loading diamond. Using offline data", Snackbar.LENGTH_LONG))
                else
                    showSnackbar(SnackbarEvent("Error while handling email. Check your login data", Snackbar.LENGTH_LONG))
            }
        }
        Log.e("MLD currentDiamond", "End handling result. Result: ${result.value}")
        return result
    }

    fun startLogoutEvent() {
        Log.e("FirebaseAuth", "Start logout. Current user: ${auth.currentUser}")
        auth.signOut()
        _logoutEvent.value = Event(Unit)
        Log.e("FirebaseAuth", "End logout. Current user: ${auth.currentUser}")
    }

    fun startFindTheDiamondEvent() {
        _findTheDiamondEvent.value = Event(Unit)
    }

    fun startGetDiamondEvent() {
        _cancelDiamondEvent.value = Event(Unit)
        _getDiamondEvent.value = Event(Unit)
    }

    fun startCancelDiamondEvent() {
        showSnackbar(SnackbarEvent("Do you want to cancel?", actionText = "Continue") {
            viewModelScope.launch(Dispatchers.Main) {
                repository.deleteCurrentDiamond()
                _cancelDiamondEvent.value = Event(Unit)
            }
        })
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

}