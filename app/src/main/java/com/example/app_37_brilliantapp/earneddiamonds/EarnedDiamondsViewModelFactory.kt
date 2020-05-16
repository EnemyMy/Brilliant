package com.example.app_37_brilliantapp.earneddiamonds

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import javax.inject.Inject

class EarnedDiamondsViewModelFactory @Inject constructor(private val repository: EarnedDiamondsRepository): BaseViewModelFactory<EarnedDiamondsViewModel> {
    override fun create(handle: SavedStateHandle): EarnedDiamondsViewModel = EarnedDiamondsViewModel(repository)
}