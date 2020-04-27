package com.example.app_37_brilliantapp.data

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val dataSource: RemoteDataSource): Repository {
    override fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>> = dataSource.observeCurrentDiamond()

    override fun observeIdeas(): LiveData<Result<List<Idea>>>  = dataSource.observeIdeas()

    override fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>> = dataSource.observeEarnedDiamonds()

    override suspend fun saveCurrentDiamond(diamond: CurrentDiamond) = dataSource.saveCurrentDiamond(diamond)

    override suspend fun deleteCurrentDiamond()  = dataSource.deleteCurrentDiamond()

    override suspend fun getCurrentDiamond(): Result<CurrentDiamond> = dataSource.getCurrentDiamond()

    override suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond) = dataSource.addEarnedDiamond(earnedDiamond)

    override suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>> = dataSource.getEarnedDiamonds()

    override suspend fun addOrChangeIdea(idea: Idea) = dataSource.addOrChangeIdea(idea)

    override suspend fun deleteIdea(idea: Idea) = dataSource.deleteIdea(idea)

    override suspend fun getIdeas(): Result<List<Idea>> = dataSource.getIdeas()

    override suspend fun clearIdeas() = dataSource.clearIdeas()

}