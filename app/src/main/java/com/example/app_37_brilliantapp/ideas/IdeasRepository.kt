package com.example.app_37_brilliantapp.ideas

import androidx.lifecycle.LiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.Idea

interface IdeasRepository {
    fun observeIdeas(): LiveData<Result<List<Idea>>>

    suspend fun addOrChangeIdea(idea: Idea)

    suspend fun deleteIdea(idea: Idea)

    suspend fun getIdeas(): Result<List<Idea>>

    suspend fun clearIdeas()
}