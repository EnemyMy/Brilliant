package com.example.app_37_brilliantapp.finddiamond

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class FindTheDiamondViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<FindTheDiamondViewModel> {
    override fun create(handle: SavedStateHandle): FindTheDiamondViewModel = FindTheDiamondViewModel(repository)
}