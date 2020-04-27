package com.example.app_37_brilliantapp.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(): RemoteDataSource {

    private val firestoreCurrentDiamondCollection = Firebase.firestore.collection("CurrentDiamond")
    private val firestoreEarnedDiamondsCollection = Firebase.firestore.collection("EarnedDiamonds")
    private val firestoreIdeasCollection = Firebase.firestore.collection("ideas")
    private val userEmail: String?
        get() { return FirebaseAuth.getInstance().currentUser?.email }

    override fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>> {
        val documentTitle = "${userEmail!!}-CurrentDiamondsDocument"
        val currentDiamond = MutableLiveData<Result<CurrentDiamond>>()
        firestoreCurrentDiamondCollection.document(documentTitle).addSnapshotListener {value, e ->
            if (e != null)
                currentDiamond.value = Result.Error(e)
            else {
                if (!value!!.exists())
                    currentDiamond.value = Result.Error(NoSuchDocumentException())
                else
                    currentDiamond.value = Result.Success(value.toObject(CurrentDiamond::class.java)!!)
            }
        }
        return currentDiamond
    }

    override fun observeIdeas(): LiveData<Result<List<Idea>>> {
        val subcollectionTitle = "${userEmail!!}-IdeasCollection"
        val subcollectionHolderTitle = "${userEmail!!}-IdeasCollectionHolder"
        val idea = MutableLiveData<Result<List<Idea>>>()
        firestoreIdeasCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).addSnapshotListener {value, e ->
            Log.e("Listener triggered!", "observeIdeas")
            if (e != null) {
                idea.value = Result.Error(e)
            }
            else {
                if (value!!.isEmpty)
                    idea.value = Result.Error(NoSuchDocumentException())
                else
                    idea.value = Result.Success(value.toObjects(Idea::class.java))
            }
        }
        return idea
    }

    override fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>> {
        val subcollectionTitle = "${userEmail!!}-EarnedDiamondsCollection"
        val subcollectionHolderTitle = "${userEmail!!}-EarnedDiamondsCollectionHolder"
        val earnedDiamond = MutableLiveData<Result<List<EarnedDiamond>>>()
        firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).addSnapshotListener {value, e ->
            if (e != null)
                earnedDiamond.value = Result.Error(e)
            else {
                if (value!!.isEmpty)
                    earnedDiamond.value = Result.Error(NoSuchDocumentException())
                else
                    earnedDiamond.value = Result.Success(value.toObjects(EarnedDiamond::class.java))
            }
        }
        return earnedDiamond
    }

    override suspend fun saveCurrentDiamond(diamond: CurrentDiamond) {
        val documentTitle = "${userEmail!!}-CurrentDiamondsDocument"
        try {
            firestoreCurrentDiamondCollection.document(documentTitle).set(diamond).await()
            Log.e("FirebaseFirestore", "saveCurrentDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "saveCurrentDiamond: $e")
        }
    }

    override suspend fun deleteCurrentDiamond() {
        val documentTitle = "${userEmail!!}-CurrentDiamondsDocument"
        try {
            firestoreCurrentDiamondCollection.document(documentTitle).delete().await()
            Log.e("FirebaseFirestore", "deleteCurrentDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "deleteCurrentDiamond: $e")
        }
    }

    override suspend fun getCurrentDiamond(): Result<CurrentDiamond> {
        val documentTitle = "${userEmail!!}-CurrentDiamondsDocument"
        return try {
            val snapshot = firestoreCurrentDiamondCollection.document(documentTitle).get().await()
            if (snapshot.exists())
                Result.Success(snapshot.toObject(CurrentDiamond::class.java)!!)
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    override suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond) {
        val subcollectionTitle = "${userEmail!!}-EarnedDiamondsCollection"
        val subcollectionHolderTitle = "${userEmail!!}-EarnedDiamondsCollectionHolder"
        val countDocumentSubcollection = "${userEmail!!}-EarnedDiamondsCollectionSize"
        val countDocumentTitle = "${userEmail!!}-EarnedDiamondsCollectionSizeDocument"
        val collectionSize = firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(countDocumentSubcollection).document(countDocumentTitle).get().await().get("count").let { it?: 0 }
        val newDocumentTitle = "${userEmail!!}-EarnedDiamondsCollection-$collectionSize"
        try {
            firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).document(newDocumentTitle).set(earnedDiamond).await()
            if (collectionSize == 0) {
                firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(countDocumentSubcollection).document(countDocumentTitle).set(mapOf(Pair("count", 1)))
            } else {
                firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(countDocumentSubcollection).document(countDocumentTitle).update("count", FieldValue.increment(1))
            }
            Log.e("FirebaseFirestore", "addEarnedDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "addEarnedDiamond: $e")
        }
    }

    override suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>> {
        val subcollectionTitle = "${userEmail!!}-EarnedDiamondsCollection"
        val subcollectionHolderTitle = "${userEmail!!}-EarnedDiamondsCollectionHolder"
        return try {
            val snapshot = firestoreEarnedDiamondsCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).get().await()
            if (!snapshot.isEmpty)
                Result.Success(snapshot.toObjects(EarnedDiamond::class.java))
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    override suspend fun addOrChangeIdea(idea: Idea) {
        val subcollectionTitle = "${userEmail!!}-IdeasCollection"
        val subcollectionHolderTitle = "${userEmail!!}-IdeasCollectionHolder"
        val newDocumentTitle = "${userEmail!!}-IdeasCollection-${idea.title}"
        try {
            firestoreIdeasCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).document(newDocumentTitle).set(idea).await()
            Log.e("FirebaseFirestore", "addOrChangeIdea: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "addOrChangeIdea: $e")
        }
    }

    override suspend fun clearIdeas() {
        val subcollectionTitle = "${userEmail!!}-IdeasCollection"
        val subcollectionHolderTitle = "${userEmail!!}-IdeasCollectionHolder"
        try {
            val ideasCollection = firestoreIdeasCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).get().await()
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
        val subcollectionTitle = "${userEmail!!}-IdeasCollection"
        val subcollectionHolderTitle = "${userEmail!!}-IdeasCollectionHolder"
        val documentTitle = "${userEmail!!}-IdeasCollection-${idea.title}"
        try {
            firestoreIdeasCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).document(documentTitle).delete().await()
            Log.e("FirebaseFirestore", "deleteIdea: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "deleteIdea: $e")
        }
    }

    override suspend fun getIdeas(): Result<List<Idea>> {
        val subcollectionTitle = "${userEmail!!}-IdeasCollection"
        val subcollectionHolderTitle = "${userEmail!!}-IdeasCollectionHolder"
        return try {
            val snapshot = firestoreIdeasCollection.document(subcollectionHolderTitle).collection(subcollectionTitle).get().await()
            if (!snapshot.isEmpty)
                Result.Success(snapshot.toObjects(Idea::class.java))
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }


}