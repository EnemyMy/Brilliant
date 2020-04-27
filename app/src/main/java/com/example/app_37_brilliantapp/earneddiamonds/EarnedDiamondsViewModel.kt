package com.example.app_37_brilliantapp.earneddiamonds

import android.util.Log
import androidx.lifecycle.*
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.google.android.material.snackbar.Snackbar

class EarnedDiamondsViewModel (private val repository: Repository): ViewModel() {

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
        if (earnedDiamonds.value != null)
            earnedDiamonds.value!!.size.toString()
        else "0"
    }

    private val _snackbarEvent = MutableLiveData<SnackbarEvent>()
    val snackbarEvent: LiveData<SnackbarEvent> = _snackbarEvent

    fun handleResult(data: Result<List<EarnedDiamond>>): LiveData<List<EarnedDiamond>> {
        val result = MutableLiveData<List<EarnedDiamond>>(null)
        when(data) {
            is Result.Success -> result.value = data.data
            is Result.Error -> {
                if (!data.noSuchDocument)
                    showSnackbar(SnackbarEvent("Error while loading earned diamonds. Using offline data", Snackbar.LENGTH_LONG))
            }
        }
        return result
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackbarEvent.value = event
    }

}