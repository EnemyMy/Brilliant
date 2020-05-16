package com.example.app_37_brilliantapp.data.ideas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.Idea
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.data.NoSuchDocumentException
import com.example.app_37_brilliantapp.util.getIdeaDocumentTitle
import com.example.app_37_brilliantapp.util.getIdeasCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseIdeasDataSource @Inject constructor(): IdeasDataSource {
    private val userEmail: String?
        get() { return FirebaseAuth.getInstance().currentUser?.email }

    override fun observeIdeas(): LiveData<Result<List<Idea>>> {
        val idea = MutableLiveData<Result<List<Idea>>>()
        val email: String = userEmail ?: return idea.apply { idea.value = Result.Error(
            InvalidEmailException()
        ) }
        val collection = Firebase.firestore.getIdeasCollection(email)
        collection.addSnapshotListener {value, e ->
            Log.e("Listener triggered!", "observeIdeas")
            if (e != null) {
                idea.value = Result.Error(e)
            }
            else {
                if (value?.isEmpty == true)
                    idea.value = Result.Success(value.toObjects(Idea::class.java))
                else
                    idea.value = Result.Error(NoSuchDocumentException())

            }
        }
        return idea
    }

    override suspend fun addOrChangeIdea(idea: Idea) {
        val email: String = userEmail ?: return
        val document = Firebase.firestore.getIdeaDocumentTitle(email, idea.title)
        try {
            document.set(idea).await()
            Log.e("FirebaseFirestore", "addOrChangeIdea: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "addOrChangeIdea: $e")
        }
    }

    override suspend fun clearIdeas() {
        val email: String = userEmail ?: return
        try {
            val ideasCollection = Firebase.firestore.getIdeasCollection(email).get().await()
            Firebase.firestore.runBatch {batch ->
                ideasCollection.documents.forEach {
                    batch.delete(it.reference)
                }
            }
            Log.e("FirebaseFirestore", "clearIdeas: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "clearIdeas: $e")
        }
    }

    override suspend fun deleteIdea(idea: Idea) {
        val email: String = userEmail ?: return
        val document = Firebase.firestore.getIdeaDocumentTitle(email, idea.title)
        try {
            document.delete().await()
            Log.e("FirebaseFirestore", "deleteIdea: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "deleteIdea: $e")
        }
    }

    override suspend fun getIdeas(): Result<List<Idea>> {
        val email: String = userEmail ?: return Result.Error(InvalidEmailException())
        val collection = Firebase.firestore.getIdeasCollection(email)
        return try {
            val snapshot = collection.get().await()
            if (!snapshot.isEmpty)
                Result.Success(snapshot.toObjects(Idea::class.java))
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}