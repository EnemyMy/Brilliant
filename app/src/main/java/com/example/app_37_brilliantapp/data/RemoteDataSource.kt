package com.example.app_37_brilliantapp.data

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result

interface RemoteDataSource {

    fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>>

    fun observeIdeas(): LiveData<Result<List<Idea>>>

    fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>>

    suspend fun saveCurrentDiamond(diamond: CurrentDiamond)

    suspend fun deleteCurrentDiamond()

    suspend fun getCurrentDiamond(): Result<CurrentDiamond>

    suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond)

    suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>>

    suspend fun addOrChangeIdea(idea: Idea)

    suspend fun deleteIdea(idea: Idea)

    suspend fun getIdeas(): Result<List<Idea>>

    suspend fun clearIdeas()
}