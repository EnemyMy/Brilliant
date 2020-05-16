package com.example.app_37_brilliantapp.ideas

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.Idea
import com.example.app_37_brilliantapp.data.ideas.IdeasDataSource
import javax.inject.Inject

class IdeasRepositoryImpl @Inject constructor(private val dataSource: IdeasDataSource): IdeasRepository {
    override fun observeIdeas(): LiveData<Result<List<Idea>>> = dataSource.observeIdeas()

    override suspend fun addOrChangeIdea(idea: Idea) = dataSource.addOrChangeIdea(idea)

    override suspend fun deleteIdea(idea: Idea) = dataSource.deleteIdea(idea)

    override suspend fun getIdeas(): Result<List<Idea>> = dataSource.getIdeas()

    override suspend fun clearIdeas() = dataSource.clearIdeas()
}