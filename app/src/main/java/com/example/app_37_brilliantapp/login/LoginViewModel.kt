package com.example.app_37_brilliantapp.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val _mainMenuEvent = MutableLiveData<Event<Unit>>()
    val mainMenuEvent: LiveData<Event<Unit>> = _mainMenuEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    //two-way databinding
    val email = MutableLiveData<String>()

    //two-way databinding
    val password = MutableLiveData<String>()

    fun startLoginEvent() {
        Log.e("FirebaseAuth", "Start login. Current user: ${auth.currentUser}. Email: ${email.value} Password: ${password.value}")
        val email = email.value ?: ""
        val password = password.value ?: ""
        if (validateFields(email, password))
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(
                        "FirebaseAuth",
                        "Login successful. Current user: ${auth.currentUser}"
                    )
                    startMainMenuEvent()
                }
                else {
                    Log.e(
                        "FirebaseAuth",
                        "Login failed. Throwable: ${task.exception}. Current user: ${auth.currentUser}"
                    )
                    showSnackbar(SnackbarEvent("Login failed"))
                }
            }
        else
            startSnackbarEvent(SnackbarEvent("Field validation failed"))
    }

    private fun showSnackbar(snackbarEvent: SnackbarEvent) {
        _snackBarEvent.value = snackbarEvent
    }

    private fun startMainMenuEvent() {
        _mainMenuEvent.value = Event(Unit)
    }

    fun startSnackbarEvent(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

    private fun validateFields(email: String, password: String): Boolean {
        return email.isNotEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.isNotEmpty()
                && password.length >= 8
                && password.contains(Regex("\\d"))
    }

}