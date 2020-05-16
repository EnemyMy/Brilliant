package com.example.app_37_brilliantapp.earneddiamonds

import android.util.Log
import androidx.lifecycle.*
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.google.firebase.auth.FirebaseAuth
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.google.android.material.snackbar.Snackbar

class EarnedDiamondsViewModel (private val repository: EarnedDiamondsRepository): ViewModel() {

    private val userEmail = MutableLiveData<String?>().apply {
        FirebaseAuth.getInstance().addAuthStateListener {
            Log.e("FirebaseAuth", "AuthStateChanged: currentUser: ${it.currentUser}")
            this.value = it.currentUser?.email
        }
    }

    val earnedDiamonds: LiveData<List<EarnedDiamond>> = userEmail.switchMap { email ->
        if (email != null && email != "") {
            repository.observeEarnedDiamonds().distinctUntilChanged().switchMap { handleResult(it) }
        }
        else MutableLiveData<List<EarnedDiamond>>(null)
    }

    val earnedDiamondsCount: LiveData<String> = earnedDiamonds.map {
        earnedDiamonds.value?.size?.toString() ?: "0"
    }

    private val _snackbarEvent = MutableLiveData<SnackbarEvent>()
    val snackbarEvent: LiveData<SnackbarEvent> = _snackbarEvent

    fun handleResult(data: Result<List<EarnedDiamond>>): LiveData<List<EarnedDiamond>> {
        val result = MutableLiveData<List<EarnedDiamond>>(null)
        when(data) {
            is Result.Success -> result.value = data.data
            is Result.Error -> {
                if (data.exception !is InvalidEmailException)
                    showSnackbar(SnackbarEvent("Error while loading earned diamonds. Using offline data", Snackbar.LENGTH_LONG))
                else
                    showSnackbar(SnackbarEvent("Error while handling email. Check your login data", Snackbar.LENGTH_LONG))
            }
        }
        return result
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackbarEvent.value = event
    }

}