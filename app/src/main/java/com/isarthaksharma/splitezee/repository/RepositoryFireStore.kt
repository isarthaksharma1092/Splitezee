package com.isarthaksharma.splitezee.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryFireStore @Inject constructor(private val firestore: FirebaseFirestore){

    suspend fun getExpensesFromFireStore(userId: String): List<PersonalDataClass> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("expenses")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(PersonalDataClass::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun checkIfEmailExists(email: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("users")
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

    suspend fun createGroup(groupName: String, createdByEmail: String, members: List<String>): Result<String> {
        val groupId = firestore.collection("groups").document().id
        val groupData = hashMapOf(
            "groupId" to groupId,
            "groupName" to groupName,
            "createdBy" to createdByEmail,
            "members" to members,
            "totalAmount" to 0.0
        )

        return try {
            firestore.collection("groups").document(groupId).set(groupData).await()
            Result.success("Group created successfully!")
        } catch (e: Exception) {
            Log.e("Firestore", "Error creating group: ${e.message}")
            Result.failure(e) // Return failure instead of throwing
        }
    }
}