package com.example.app_37_brilliantapp.currentdiamond

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class CurrentDiamondViewModelFactory @Inject constructor(private val repository: CurrentDiamondRepository): BaseViewModelFactory<CurrentDiamondViewModel> {
    override fun create(handle: SavedStateHandle): CurrentDiamondViewModel = CurrentDiamondViewModel(repository)
}