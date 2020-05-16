package com.example.app_37_brilliantapp.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class SignupViewModel: ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _mainMenuEvent = MutableLiveData<Event<Unit>>()
    val mainMenuEvent: LiveData<Event<Unit>> = _mainMenuEvent

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    //two-way databinding
    val email = MutableLiveData<String>()

    //two-way databinding
    val password = MutableLiveData<String>()

    //two-way databinding
    val name = MutableLiveData<String>()

    fun startLoginEvent() {
        _loginEvent.value = Event(Unit)
    }

    fun startSignupEvent() {
        Log.e("FirebaseAuth", "Start signup. Current user: ${auth.currentUser}. Email: ${email.value} Password: ${password.value}")
        val email = email.value ?: ""
        val password = password.value ?: ""
        val name = name.value ?: ""
        if (validateFields(email, password, name)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(
                            "FirebaseAuth",
                            "Signup successful. Current user: ${auth.currentUser} UserName: ${auth.currentUser?.displayName}"
                        )
                        auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
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
        _mainMenuEvent.value = Event(Unit)
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

    private fun validateFields(email: String, password: String, name: String): Boolean {
        return email.isNotEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.isNotEmpty()
                && password.length >= 8
                && password.contains(Regex("\\d"))
                && name.isNotEmpty()
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("FirebaseAuth", "firebaseAuthWithGoogle:" + acct.id)
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