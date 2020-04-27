package com.example.app_37_brilliantapp.signup

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class SignupViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<SignupViewModel> {

    override fun create(handle: SavedStateHandle): SignupViewModel = SignupViewModel(repository)

}