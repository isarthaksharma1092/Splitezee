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
import com.isarthaksharma.splitezee.repository.RepositoryPersonalDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelFireStore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val repositoryFireStore: RepositoryFireStore,
    private val repositoryPersonalDB:RepositoryPersonalDB
): ViewModel(

){
    // User Exist Check
    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists:StateFlow<Boolean?> = _emailExists

    private val _groups = MutableStateFlow<List<GroupDataClass>>(emptyList())
    val groups: StateFlow<List<GroupDataClass>> = _groups

    private val _groupCreationStatus = MutableStateFlow<String?>(null)
    val groupCreationStatus:StateFlow<String?> = _groupCreationStatus

    fun checkEmail(email: String) {
        viewModelScope.launch {
            _emailExists.emit(repositoryFireStore.checkIfEmailExists(email))
        }
    }

    fun uploadPersonalExpense(expense:PersonalDataClass){
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStore.uploadPersonalExpense(
                userId = userId,
                expense = expense
            )
        }
    }
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
}