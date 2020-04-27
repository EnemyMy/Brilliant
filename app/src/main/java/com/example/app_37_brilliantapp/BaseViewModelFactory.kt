package com.example.app_37_brilliantapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface BaseViewModelFactory<T : ViewModel> {

    fun create(handle: SavedStateHandle): T

}