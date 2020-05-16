package com.example.app_37_brilliantapp.data.earneddiamonds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.data.NoSuchDocumentException
import com.example.app_37_brilliantapp.util.getEarnedDiamondsCollection
import com.example.app_37_brilliantapp.util.getEarnedDiamondsCountDocument
import com.example.app_37_brilliantapp.util.getEarnedDiamondsDocumentTitle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseEarnedDiamondsDataSource @Inject constructor(): EarnedDiamondsDataSource {
    private val userEmail: String?
        get() { return FirebaseAuth.getInstance().currentUser?.email }

    override fun observeEarnedDiamonds(): LiveData<Result<List<EarnedDiamond>>> {
        val earnedDiamond = MutableLiveData<Result<List<EarnedDiamond>>>()
        val email: String = userEmail ?: return earnedDiamond.apply { value = Result.Error(
            InvalidEmailException()
        ) }
        val collection = Firebase.firestore.getEarnedDiamondsCollection(email)
        collection.addSnapshotListener {value, e ->
            if (e != null)
                earnedDiamond.value = Result.Error(e)
            else {
                if (value?.isEmpty == false)
                    earnedDiamond.value = Result.Success(value.toObjects(EarnedDiamond::class.java))
                else
                    earnedDiamond.value = Result.Error(NoSuchDocumentException())
            }
        }
        return earnedDiamond
    }

    override suspend fun addEarnedDiamond(earnedDiamond: EarnedDiamond) {
        val email: String = userEmail ?: return
        val countDocument = Firebase.firestore.getEarnedDiamondsCountDocument(email)
        val collectionSize = countDocument.get().await().get("count").let { it?: 0 }
        val earnedDiamondDocument = Firebase.firestore.getEarnedDiamondsDocumentTitle(email, collectionSize as Long)
        try {
            earnedDiamondDocument.set(earnedDiamond).await()
            if (collectionSize == 0) {
                countDocument.set(mapOf(Pair("count", 1)))
            } else {
                countDocument.update("count", FieldValue.increment(1))
            }
            Log.e("FirebaseFirestore", "addEarnedDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "addEarnedDiamond: $e")
        }
    }

    override suspend fun getEarnedDiamonds(): Result<List<EarnedDiamond>> {
        val email: String = userEmail ?: return Result.Error(InvalidEmailException())
        val collection = Firebase.firestore.getEarnedDiamondsCollection(email)
        return try {
            val snapshot = collection.get().await()
            if (!snapshot.isEmpty)
                Result.Success(snapshot.toObjects(EarnedDiamond::class.java))
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}