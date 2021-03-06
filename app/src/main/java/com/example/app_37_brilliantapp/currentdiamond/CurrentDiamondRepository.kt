package com.example.app_37_brilliantapp.currentdiamond

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond

interface CurrentDiamondRepository {
    fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>>

    suspend fun saveCurrentDiamond(diamond: CurrentDiamond)

    suspend fun deleteCurrentDiamond()

    suspend fun getCurrentDiamond(): Result<CurrentDiamond>
}