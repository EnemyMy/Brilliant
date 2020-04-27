package com.example.app_37_brilliantapp.signup

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
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class SignupViewModel (private val repository: Repository): ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    private val _loginEvent = MutableLiveData<Unit>()
    val loginEvent: LiveData<Unit> = _loginEvent

    private val _mainMenuEvent = MutableLiveData<Unit>()
    val mainMenuEvent: LiveData<Unit> = _mainMenuEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    //two-way databinding
    val email = MutableLiveData<String>()

    //two-way databinding
    val password = MutableLiveData<String>()

    //two-way databinding
    val name = MutableLiveData<String>()

    fun startLoginEvent() {
        _loginEvent.value = Unit
    }

    fun startSignupEvent() {
        Log.e("FirebaseAuth", "Start signup. Current user: ${auth.currentUser}. Email: ${email.value} Password: ${password.value}")
        if (validateFields()) {
            auth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(
                            "FirebaseAuth",
                            "Signup successful. Current user: ${auth.currentUser} UserName: ${auth.currentUser?.displayName}"
                        )
                        auth.currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name.value).build())
                        startMainMenuEvent()
                    } else {
                        Log.e(
                            "FirebaseAuth",
                            "Signup failed. Throwable: ${task.exception}. Current user: ${auth.currentUser}"
                        )
                        showSnackbar(SnackbarEvent("Signup failed"))
                    }
                }
        }
        else
            showSnackbar(SnackbarEvent("Field validation failed"))
    }

    fun startMainMenuEvent() {
        Log.e("FirebaseAuth", "User logged in. Current user: ${auth.currentUser}")
        _mainMenuEvent.value = Unit
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

    private fun validateFields(): Boolean {
        if (email.value != null && password.value != null && name.value != null)
            return email.value!!.isValidEmail() && password.value!!.isValidPassword() && name.value!!.isValidName()
        return false
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("FirebaseAuth", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.e(
                    "FirebaseAuth",
                    "GoogleSignIn successful. Current user: ${auth.currentUser} UserName: ${auth.currentUser?.displayName}"
                )
                startMainMenuEvent()
            } else {
                // If sign in fails, display a message to the user.
                Log.e(
                    "FirebaseAuth",
                    "GoogleSignIn failed. Throwable: ${task.exception}. Current user: ${auth.currentUser}"
                )
                if (task.exception is com.google.firebase.auth.FirebaseAuthUserCollisionException)
                    showSnackbar(SnackbarEvent("Account with this email already exist. Please use it"))
                else
                    showSnackbar(SnackbarEvent("Google sign in failed"))
            }
        }
    }

    fun firebaseAuthWithFacebook(token: AccessToken) {
        Log.d("FirebaseAuth", "firebaseAuthWithFacebook: $token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.e(
                    "FirebaseAuth",
                    "FacebookSignIn successful. Current user: ${auth.currentUser} UserName: ${auth.currentUser?.displayName}"
                )
                startMainMenuEvent()
            } else {
                // If sign in fails, display a message to the user.
                Log.e(
                    "FirebaseAuth",
                    "FacebookSignIn failed. Throwable: ${task.exception}. Current user: ${auth.currentUser}"
                )
                if (task.exception is com.google.firebase.auth.FirebaseAuthUserCollisionException)
                    showSnackbar(SnackbarEvent("Account with this email already exist. Please use it"))
                else
                    showSnackbar(SnackbarEvent("Facebook sign in failed"))
            }
        }
    }

}