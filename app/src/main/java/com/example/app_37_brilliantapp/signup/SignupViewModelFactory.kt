package com.example.app_37_brilliantapp.signup

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class SignupViewModelFactory @Inject constructor(): BaseViewModelFactory<SignupViewModel> {

    override fun create(handle: SavedStateHandle): SignupViewModel = SignupViewModel()

}