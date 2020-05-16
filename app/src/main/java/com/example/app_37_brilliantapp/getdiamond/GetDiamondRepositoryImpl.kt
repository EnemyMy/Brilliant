package com.example.app_37_brilliantapp.getdiamond

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.data.curentdiamond.CurrentDiamondDataSource
import com.example.app_37_brilliantapp.data.earneddiamonds.EarnedDiamondsDataSource
import javax.inject.Inject

class GetDiamondRepositoryImpl @Inject constructor(private val currentDiamondDataSource: CurrentDiamondDataSource, private val earnedDiamondDataSource: EarnedDiamondsDataSource): GetDiamondRepository {
    override fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>> = currentDiamondDataSource.observeCurrentDiamond()

    override fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>> = earnedDiamondDataSource.observeEarnedDiamonds()

    override suspend fun saveCurrentDiamond(diamond: CurrentDiamond) = currentDiamondDataSource.saveCurrentDiamond(diamond)

    override suspend fun deleteCurrentDiamond()  = currentDiamondDataSource.deleteCurrentDiamond()

    override suspend fun getCurrentDiamond(): Result<CurrentDiamond> = currentDiamondDataSource.getCurrentDiamond()

    override suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond) = earnedDiamondDataSource.addEarnedDiamond(earnedDiamond)

    override suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>> = earnedDiamondDataSource.getEarnedDiamonds()
}