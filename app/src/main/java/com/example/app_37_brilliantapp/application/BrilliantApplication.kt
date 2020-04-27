package com.example.app_37_brilliantapp.application

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class BrilliantApplication: Application() {

    val appComponent by lazy { DaggerAppComponent.factory().create(this) }

}