package com.example.app_37_brilliantapp.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.example.app_37_brilliantapp.util.isValidEmail
import com.example.app_37_brilliantapp.util.isValidName
import com.example.app_37_brilliantapp.util.isValidPassword
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginViewModel (private val repository: Repository): ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val _mainMenuEvent = MutableLiveData<Unit>()
    val mainMenuEvent: LiveData<Unit> = _mainMenuEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    //two-way databinding
    val email = MutableLiveData<String>()

    //two-way databinding
    val password = MutableLiveData<String>()

    fun startLoginEvent() {
        Log.e("FirebaseAuth", "Start login. Current user: ${auth.currentUser}. Email: ${email.value} Password: ${password.value}")
        if (validateFields())
            auth.signInWithEmailAndPassword(email.value!!, password.value!!).addOnCompleteListener { task ->
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
        _mainMenuEvent.value = Unit
    }

    fun startSnackbarEvent(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

    private fun validateFields(): Boolean {
        if (email.value != null && password.value != null)
            return email.value!!.isValidEmail() && password.value!!.isValidPassword()
        return false
    }

}