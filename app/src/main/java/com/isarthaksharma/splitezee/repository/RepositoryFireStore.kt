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

//    fun fetchUserInfoByEmail(
//        email: String,
//        onResult: (Boolean, String?, String?, String?) -> Unit
//    ) {
//        FirebaseFirestore.getInstance().collection("users")
//            .whereEqualTo("email", email)
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    Log.e("FirestoreDebug", "Error fetching user info: ${error.message}", error)
//                    onResult(false, null, null, null)
//                    return@addSnapshotListener
//                }
//
//                Log.d("FirestoreDebug", "Query result: ${value?.documents}")
//
//                val doc = value?.documents?.firstOrNull()
//                if (doc != null) {
//                    val name = doc.getString("name")
//                    val profileImage = doc.getString("profilePic")
//                    val userId = doc.getString("userId")
//                    onResult(true, name, profileImage, userId)
//                } else {
//                    onResult(false, null, null, null)
//                }
//            }
//    }


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


    suspend fun createGroup(
        groupName: String,
        createdByEmail: String,
        members: List<String>
    ): Result<String> {
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
            Log.e("FireStore", "Error creating group: ${e.message}")
            Result.failure(e)
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
}