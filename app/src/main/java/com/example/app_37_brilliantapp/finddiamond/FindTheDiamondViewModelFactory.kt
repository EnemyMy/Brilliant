package com.example.app_37_brilliantapp.finddiamond

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class FindTheDiamondViewModelFactory @Inject constructor(private val repository: FindDiamondRepository): BaseViewModelFactory<FindTheDiamondViewModel> {
    override fun create(handle: SavedStateHandle): FindTheDiamondViewModel = FindTheDiamondViewModel(repository)
}