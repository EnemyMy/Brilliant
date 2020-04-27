package com.example.app_37_brilliantapp.earneddiamonds

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class EarnedDiamondsViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<EarnedDiamondsViewModel> {
    override fun create(handle: SavedStateHandle): EarnedDiamondsViewModel = EarnedDiamondsViewModel(repository)
}