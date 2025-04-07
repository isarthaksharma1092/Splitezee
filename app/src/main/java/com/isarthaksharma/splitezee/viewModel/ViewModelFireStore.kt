package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.repository.RepositoryFireStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelFireStore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val repositoryFireStore: RepositoryFireStore
) : ViewModel() {
    // User Exist Check
    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists: StateFlow<Boolean?> = _emailExists

    private val _groups = MutableStateFlow<List<GroupDataClass>>(emptyList())
    val groups: StateFlow<List<GroupDataClass>> = _groups

    private val _groupCreationStatus = MutableStateFlow<String?>(null)
    val groupCreationStatus: StateFlow<String?> = _groupCreationStatus

    // *************** Personal ***************
    fun uploadPersonalExpense(expense: PersonalDataClass, expenseId: String) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStore.uploadPersonalExpense(
                userId = userId,
                expense = expense,
                expenseId = expenseId
            )
        }
    }

    fun removePersonalExpense(expenseID: String) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStore.removePersonalExpense(userId, expenseID)
        }
    }

    // *************** Group ***************
    fun createGroup(groupName: String, members: List<String>) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        val groupId = firestore.collection("groups").document().id

        val groupData = hashMapOf(
            "groupId" to groupId,
            "groupName" to groupName,
            "createdBy" to userEmail,
            "members" to members + userEmail,
            "totalAmount" to 0.0
        )

        viewModelScope.launch {
            try {
                firestore.collection("groups").document(groupId).set(groupData)
                _groupCreationStatus.value = "Group created successfully!"
            } catch (e: Exception) {
                _groupCreationStatus.value = "Error creating group: ${e.message}"
            }
        }
    }

    /*
    fun fetchUserInfoByEmail(
        email: String,
        onResult: (GroupMemberDataClass) -> Unit
    ) {
        firestoreI.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                val doc = documents.firstOrNull()
                val currentUser = FirebaseAuth.getInstance().currentUser

                val member = GroupMemberDataClass(
                    groupId = "",
                    userId = doc?.getString("userID") ?: if (email == currentUser?.email) currentUser.uid else null,
                    email = email,
                    displayName = doc?.getString("name") ?: email.substringBefore("@"),
                    profileImage = doc?.getString("profilePic"),
                    registered = true
                )
                onResult(member)
            }
            .addOnFailureListener {
                val member = GroupMemberDataClass(
                    groupId = "",
                    userId = null,
                    email = email,
                    displayName = email.substringBefore("@"),
                    profileImage = null,
                    registered = false
                )
                onResult(member)
            }
    }
     */

    fun fetchUserInfoByEmail(
        email: String,
        onResult: (String?, String?, String?) -> Unit // name, profilePic, userId
    ) {
        repositoryFireStore.fetchUserInfoByEmail(email) { success, name, profilePic, userId ->
            if (success) {
                onResult(name, profilePic, userId)
            } else {
                onResult(null, null, null)
            }
        }
    }

    fun updatePersonalExpense(expense: PersonalDataClass) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStore.updatePersonalExpense(userId, expense)
        }
    }
}