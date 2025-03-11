package com.isarthaksharma.splitezee.viewModel

//import androidx.compose.ui.tooling.data.Group
//import androidx.compose.ui.tooling.data.UiToolingDataApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.toObjects
//import com.isarthaksharma.splitezee.repository.RepositorySyncing
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//@OptIn(UiToolingDataApi::class)
//@HiltViewModel
//class ViewModelSyncing @Inject constructor(
//    private val firestore: FirebaseFirestore,
//    private val repositorySyncing : RepositorySyncing,
//    private val viewModelPersonalDB: ViewModelPersonalDB) : ViewModel() {
//
//    private val _emailExists = MutableStateFlow<Boolean?>(null)
//    val emailExists: StateFlow<Boolean?> = _emailExists
//
//    private val _groups = MutableStateFlow<List<Group>>(emptyList())
//    val groups: StateFlow<List<Group>> = _groups
//
//    private val _groupCreationStatus = MutableStateFlow<String?>(null)
//    val groupCreationStatus: StateFlow<String?> = _groupCreationStatus
//
//    init {
//        fetchGroupsForUser()
//    }
//
//    fun checkEmail(email: String) {
//        viewModelScope.launch {
//            _emailExists.value = repositorySyncing.checkIfEmailExists(email)
//        }
//    }
//
//    suspend fun addPersonalExpense(userId: String, expense:) {
//        firestore.collection("users").document(userId)
//            .collection("personalExpenses").document()
//            .set(expense).await()
//    }
//
//    private fun fetchGroupsForUser() {
//        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
//        firestore.collection("groups")
//            .whereArrayContains("members", userEmail)
//            .addSnapshotListener { snapshot, _ ->
//                snapshot?.let {
//                    _groups.value = it.toObjects()
//                }
//            }
//    }
//
//    fun createGroup(groupName: String, members: List<String>) {
//        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
//        val groupId = firestore.collection("groups").document().id
//
//        val groupData = hashMapOf(
//            "groupId" to groupId,
//            "groupName" to groupName,
//            "createdBy" to userEmail,
//            "members" to members + userEmail, // Add creator to members list
//            "totalAmount" to 0.0
//        )
//
//        viewModelScope.launch {
//            try {
//                firestore.collection("groups").document(groupId).set(groupData)
//                _groupCreationStatus.value = "Group created successfully!"
//            } catch (e: Exception) {
//                _groupCreationStatus.value = "Error creating group: ${e.message}"
//            }
//        }
//    }
//}