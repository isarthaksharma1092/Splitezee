package com.isarthaksharma.splitezee.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryFireStore @Inject constructor(private val firestore: FirebaseFirestore) {
    fun fetchUserInfoByEmail(
        email: String,
        onResult: (Boolean, String?, String?, String?) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                val doc = result.documents.firstOrNull()
                if (doc != null) {
                    val name = doc.getString("name")
                    val profileImage = doc.getString("profilePic")
                    val userId = doc.getString("userId")
                    onResult(true, name, profileImage, userId)
                } else {
                    onResult(false, null, null, null)
                }
            }
            .addOnFailureListener {
                onResult(false, null, null, null)
            }
    }

    suspend fun uploadPersonalExpense(
        userId: String,
        expense: PersonalDataClass,
        expenseId: String
    ) {
        val firestoreRef = firestore.collection("users")
            .document(userId)
            .collection("expenses")
            .document(expenseId)

        try {
            firestoreRef.set(expense).await()
            Log.d("FireStore", "Expense added successfully with custom ID!")
        } catch (e: Exception) {
            Log.e("FireStore", "Failed to add expense: ${e.message}")
        }
    }

    suspend fun removePersonalExpense(userId: String, expenseId: String) {
        Log.d("Debug", "Attempting to delete expense with ID: $expenseId for user: $userId")

        val firestoreRef = firestore.collection("users")
            .document(userId)
            .collection("expenses")
            .document(expenseId)
        try {
            firestoreRef.delete().await()
            Log.d(
                "FireStore",
                "Expense deleted successfully from Firestore for user: $userId, ExpenseID: $expenseId"
            )
        } catch (e: Exception) {
            Log.e(
                "FireStore",
                "Error deleting expense for user: $userId, ExpenseID: $expenseId - ${e.message}"
            )
        }
    }

    suspend fun updatePersonalExpense(userId: String, expense: PersonalDataClass) {
        val firestoreRef = firestore.collection("users")
            .document(userId)
            .collection("expenses")
            .document(expense.expenseId)

        try {
            firestoreRef.set(expense).await()
            Log.d(
                "FireStore",
                "Expense updated successfully for user: $userId, ExpenseID: ${expense.expenseId}"
            )
        } catch (e: Exception) {
            Log.e("FireStore", "Error updating expense in Firestore: ${e.message}")
        }
    }

// ************************************* Group Upload *************************************

    // Uploading GroupId for Each User
    suspend fun uploadGroupId(userId: String, groupId: String) {
        val firestoreRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("groups")
            .document(groupId)

        val data = mapOf(
            "groupId" to groupId,
            "joinedAt" to System.currentTimeMillis()
        )
        try {
            firestoreRef.set(data).await()
            Log.d("FireStore", "GroupId added successfully !")
        } catch (e: Exception) {
            Log.e("FireStore", "Failed to add GroupID: ${e.message}")
        }
    }

    // Uploading Group Info
    suspend fun createGroup(groupId:String, groupName: String,adminName:String,groupCreation:Long) {
        val firestoreRef = FirebaseFirestore.getInstance()
            .collection("groups_collection")
            .document(groupId)

        val data = mapOf(
            "groupName" to groupName,
            "adminName" to adminName,
            "groupCreation" to groupCreation
        )

        try {
            firestoreRef.set(data).await()
            Log.d("FireStore", "Group created successfully !")
        } catch (e: Exception) {
            Log.e("FireStore", "Failed to add Group: ${e.message}")
        }
    }
}