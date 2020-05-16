package com.example.app_37_brilliantapp.login

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(): BaseViewModelFactory<LoginViewModel> {
    override fun create(handle: SavedStateHandle): LoginViewModel = LoginViewModel()
}