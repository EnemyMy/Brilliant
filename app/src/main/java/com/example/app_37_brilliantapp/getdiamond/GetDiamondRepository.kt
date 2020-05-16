package com.example.app_37_brilliantapp.getdiamond

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.EarnedDiamond

interface GetDiamondRepository {
    fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>>

    fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>>

    suspend fun saveCurrentDiamond(diamond: CurrentDiamond)

    suspend fun deleteCurrentDiamond()

    suspend fun getCurrentDiamond(): Result<CurrentDiamond>

    suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond)

    suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>>
}