package com.example.app_37_brilliantapp.data.curentdiamond

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_37_brilliantapp.Result
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.data.InvalidEmailException
import com.example.app_37_brilliantapp.data.NoSuchDocumentException
import com.example.app_37_brilliantapp.util.getCurrentDiamondDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCurrentDiamondDataSource @Inject constructor(): CurrentDiamondDataSource {
    private val userEmail: String?
        get() { return FirebaseAuth.getInstance().currentUser?.email }

    override fun observeCurrentDiamond(): LiveData<Result<CurrentDiamond>> {
        val currentDiamond = MutableLiveData<Result<CurrentDiamond>>()
        val email: String = userEmail ?: return currentDiamond.apply { value = Result.Error(
            InvalidEmailException()
        ) }
        val document = Firebase.firestore.getCurrentDiamondDocument(email)
        document.addSnapshotListener {value, e ->
            if (e != null)
                currentDiamond.value = Result.Error(e)
            else {
                if (value?.exists() == true)
                    currentDiamond.value = Result.Success(value.toObject(CurrentDiamond::class.java)!!)
                else
                    currentDiamond.value = Result.Error(NoSuchDocumentException())
            }
        }
        return currentDiamond
    }

    override suspend fun saveCurrentDiamond(diamond: CurrentDiamond) {
        val email: String = userEmail ?: return
        val document = Firebase.firestore.getCurrentDiamondDocument(email)
        try {
            document.set(diamond).await()
            Log.e("FirebaseFirestore", "saveCurrentDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "saveCurrentDiamond: $e")
        }
    }

    override suspend fun deleteCurrentDiamond() {
        val email: String = userEmail ?: return
        val document = Firebase.firestore.getCurrentDiamondDocument(email)
        try {
            document.delete().await()
            Log.e("FirebaseFirestore", "deleteCurrentDiamond: Success")
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirebaseFirestore", "deleteCurrentDiamond: $e")
        }
    }

    override suspend fun getCurrentDiamond(): Result<CurrentDiamond> {
        val email: String = userEmail ?: return Result.Error(InvalidEmailException())
        val document = Firebase.firestore.getCurrentDiamondDocument(email)
        return try {
            val snapshot = document.get().await()
            if (snapshot.exists())
                Result.Success(snapshot.toObject(CurrentDiamond::class.java)!!)
            else
                Result.Error(NoSuchDocumentException())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}