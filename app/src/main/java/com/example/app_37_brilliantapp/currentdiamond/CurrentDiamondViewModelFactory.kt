package com.example.app_37_brilliantapp.currentdiamond

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class CurrentDiamondViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<CurrentDiamondViewModel> {
    override fun create(handle: SavedStateHandle): CurrentDiamondViewModel = CurrentDiamondViewModel(repository)
}