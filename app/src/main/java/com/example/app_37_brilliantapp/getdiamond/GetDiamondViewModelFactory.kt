package com.example.app_37_brilliantapp.getdiamond

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class GetDiamondViewModelFactory @Inject constructor(private val repository: GetDiamondRepository): BaseViewModelFactory<GetDiamondViewModel> {
    override fun create(handle: SavedStateHandle): GetDiamondViewModel = GetDiamondViewModel(repository)
}