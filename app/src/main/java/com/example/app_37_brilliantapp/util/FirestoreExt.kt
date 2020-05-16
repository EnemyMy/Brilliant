package com.example.app_37_brilliantapp.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.getCurrentDiamondDocument(email: String): DocumentReference {
    return collection("CurrentDiamond").document("$email-CurrentDiamondsDocument")
}

fun FirebaseFirestore.getIdeasCollection(email: String): CollectionReference {
    return collection("ideas").document("$email-IdeasCollectionHolder").collection("$email-IdeasCollection")
}

fun FirebaseFirestore.getIdeaDocumentTitle(email: String, ideaTitle: String): DocumentReference {
    return getIdeasCollection(email).document("$email-IdeasCollection-$ideaTitle")
}

fun FirebaseFirestore.getEarnedDiamondsCollection(email: String): CollectionReference {
    return collection("EarnedDiamonds").document("$email-EarnedDiamondsCollectionHolder").collection("$email-EarnedDiamondsCollection")
}

fun FirebaseFirestore.getEarnedDiamondsDocumentTitle(email: String, collectionSize: Long): DocumentReference {
    return getEarnedDiamondsCollection(email).document("$email-EarnedDiamondsCollection-$collectionSize")
}

fun FirebaseFirestore.getEarnedDiamondsCountDocument(email: String): DocumentReference {
    return collection("EarnedDiamonds").document("$email-EarnedDiamondsCollectionHolder").collection("$email-EarnedDiamondsCollectionSize").document("$email-EarnedDiamondsCollectionSizeDocument")
}