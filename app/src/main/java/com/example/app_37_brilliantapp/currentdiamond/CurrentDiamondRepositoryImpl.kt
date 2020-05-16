package com.example.app_37_brilliantapp.currentdiamond

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.curentdiamond.CurrentDiamondDataSource
import javax.inject.Inject

class CurrentDiamondRepositoryImpl @Inject constructor(private val dataSource: CurrentDiamondDataSource): CurrentDiamondRepository {
    override fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>> = dataSource.observeCurrentDiamond()

    override suspend fun saveCurrentDiamond(diamond: CurrentDiamond) = dataSource.saveCurrentDiamond(diamond)

    override suspend fun deleteCurrentDiamond()  = dataSource.deleteCurrentDiamond()

    override suspend fun getCurrentDiamond(): Result<CurrentDiamond> = dataSource.getCurrentDiamond()
}