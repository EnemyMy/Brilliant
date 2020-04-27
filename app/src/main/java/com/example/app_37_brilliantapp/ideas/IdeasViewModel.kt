package com.example.app_37_brilliantapp.ideas

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.*
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Idea
import com.example.app_37_brilliantapp.data.Repository
import com.example.app_37_brilliantapp.util.SnackbarEvent
import com.example.app_37_brilliantapp.util.toCalendar
import com.example.app_37_brilliantapp.util.toDateOrToday
import com.example.app_37_brilliantapp.util.toFormattedString
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class IdeasViewModel (private val repository: Repository): ViewModel() {

    private val userEmail = MutableLiveData<String?>().apply {
        FirebaseAuth.getInstance().addAuthStateListener {
            Log.e("FirebaseAuth", "AuthStateChanged: currentUser: ${it.currentUser}")
            this.value = it.currentUser?.email
        }
    }

    val ideas: LiveData<List<Idea>> = userEmail.switchMap { email ->
        if (email != null || email != "") {
            repository.observeIdeas().switchMap { handleResult(it) }
        }
        else MutableLiveData<List<Idea>>(null)
    }

    private val _noIdeas: MutableLiveData<Boolean> = ideas.map {
        ideas.value == null || ideas.value!!.isEmpty()
    } as MutableLiveData<Boolean>
    val noIdeas: LiveData<Boolean> = _noIdeas

    private val _snackBarEvent = MutableLiveData<SnackbarEvent>()
    val snackBarEvent: LiveData<SnackbarEvent> = _snackBarEvent

    //two-way databinding
    val newIdeaName = MutableLiveData<String>()

    private fun handleResult(data: Result<List<Idea>>): LiveData<List<Idea>> {
        val result = MutableLiveData<List<Idea>>(null)
        when(data) {
            is Result.Success -> result.value = data.data
            is Result.Error -> {
                if (!data.noSuchDocument)
                    showSnackbar(SnackbarEvent("Error while loading ideas. Using offline data", Snackbar.LENGTH_LONG))
            }
        }
        Log.e("HandleResult", "Result: ${result.value}")
        return result
    }

    fun createIdea(): Boolean {
        if (newIdeaName.value == null || newIdeaName.value == "") {
            showSnackbar(SnackbarEvent("Please enter correct idea"))
            return false
        }
        val list = ideas.value
        if (ideas.value != null && list!!.any { idea -> idea.title == newIdeaName.value }) {
            showSnackbar(SnackbarEvent("You already have this idea"))
            return false
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrChangeIdea(Idea(newIdeaName.value!!, createTime = Calendar.getInstance().toFormattedString()))
        }
        return true
    }

    fun changeIdeaState(idea: Idea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrChangeIdea(idea)
        }
    }

    fun clearOldIdeas(): Boolean {
        if (ideas.value != null && ideas.value!!.isNotEmpty()) {
            //checking if first idea was made yesterday. No need to check all ideas
            val firstIdea = ideas.value!!.toMutableList()[0]
            val wasMadeToday = DateUtils.isToday(firstIdea.createTime.toDateOrToday().time)
            if (!wasMadeToday) {
                viewModelScope.launch(Dispatchers.IO) { repository.clearIdeas() }
                return true
            }
        }
        return false
    }

    fun deleteIdea(idea: Idea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteIdea(idea)
        }
    }

    fun showSnackbar(event: SnackbarEvent) {
        _snackBarEvent.value = event
    }

}