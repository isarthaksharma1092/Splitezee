package com.isarthaksharma.splitezee.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.util.Log

class RepositorySyncing @Inject constructor(private val firestore: FirebaseFirestore) {



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
