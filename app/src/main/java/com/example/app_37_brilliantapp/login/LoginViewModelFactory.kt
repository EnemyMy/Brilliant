package com.example.app_37_brilliantapp.login

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<LoginViewModel> {
    override fun create(handle: SavedStateHandle): LoginViewModel = LoginViewModel(repository)
}