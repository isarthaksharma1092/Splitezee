package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun updatePersonalExpense(expense: PersonalDataClass) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStore.updatePersonalExpense(userId, expense)
        }
    }

// ************************************* Group Info *************************************

    fun uploadGroupId(userId: String,groupId:String){
        viewModelScope.launch {
            repositoryFireStore.uploadGroupId(userId, groupId)
        }
    }

    fun fetchUserInfoByEmail(
        email: String,
        onResult: (String?, String?, String?) -> Unit
    ) {
        repositoryFireStore.fetchUserInfoByEmail(email) { success, name, profilePic, userId ->
            if (success) {
                onResult(name, profilePic, userId)
            } else {
                onResult(null, null, null)
            }
        }
    }

    fun createGroup(groupId:String, groupName: String,adminName:String,groupCreation:Long) {
        viewModelScope.launch {
            repositoryFireStore.createGroup(groupId, groupName, adminName, groupCreation)
        }
    }


}