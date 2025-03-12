package com.isarthaksharma.splitezee.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryFireStoreUpload @Inject constructor(private val firestore: FirebaseFirestore){

    suspend fun checkIfEmailExists(email: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("AppUsers")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FireStore", "Error checking email existence: ${e.message}")
            false
        }
    }

    suspend fun uploadPersonalExpense(userId: String, expense: PersonalDataClass) {
        try {
            val expenseRef = firestore.collection("users")
                .document(userId)
                .collection("expenses")
                .document()

            expenseRef.set(expense.copy(firestoreExpenseId = expenseRef.id)).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error saving expense: ${e.message}")
        }
    }
}