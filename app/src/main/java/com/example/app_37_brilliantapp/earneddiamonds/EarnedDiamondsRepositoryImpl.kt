package com.example.app_37_brilliantapp.earneddiamonds

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.data.earneddiamonds.EarnedDiamondsDataSource
import javax.inject.Inject

class EarnedDiamondsRepositoryImpl @Inject constructor(private val dataSource: EarnedDiamondsDataSource): EarnedDiamondsRepository {
    override fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>> = dataSource.observeEarnedDiamonds()

    override suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond) = dataSource.addEarnedDiamond(earnedDiamond)

    override suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>> = dataSource.getEarnedDiamonds()
}