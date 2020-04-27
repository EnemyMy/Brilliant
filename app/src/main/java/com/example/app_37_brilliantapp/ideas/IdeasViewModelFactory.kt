package com.example.app_37_brilliantapp.ideas

import androidx.lifecycle.SavedStateHandle
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.data.Repository
import javax.inject.Inject

class IdeasViewModelFactory @Inject constructor(private val repository: Repository): BaseViewModelFactory<IdeasViewModel> {
    override fun create(handle: SavedStateHandle): IdeasViewModel = IdeasViewModel(repository)
}