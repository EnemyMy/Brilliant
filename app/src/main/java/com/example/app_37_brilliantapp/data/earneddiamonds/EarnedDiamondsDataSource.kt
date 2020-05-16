package com.example.app_37_brilliantapp.data.earneddiamonds

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.EarnedDiamond

interface EarnedDiamondsDataSource {

    fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>>

    suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond)

    suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>>

}