package com.isarthaksharma.splitezee.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.repository.RepositoryGroupDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroupDetail @Inject constructor(
    private val repository: RepositoryGroupDetail
) : ViewModel() {
    // ****************** GROUP DETAILS *****************
    private val _groupDetails = MutableStateFlow<GroupDetailDataClass?>(null)
    val groupDetails: StateFlow<GroupDetailDataClass?> = _groupDetails

    fun fetchGroupDetails(groupId: String) {
        Log.d("DEBUG", "All groups in DB: $groupId")
        viewModelScope.launch {
            repository.getGroupDetailById(groupId).collect { groupData ->
                _groupDetails.value = groupData
            }
        }
    }

    fun insertGroup(group: GroupDetailDataClass) {
        viewModelScope.launch {
            repository.insertGroup(group)
        }
    }

    fun updateGroup(group: GroupDetailDataClass) {
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            repository.deleteGroup(groupId)
        }
    }

    fun deleteAllGroups() {
        viewModelScope.launch {
            repository.deleteAllGroups()
        }
    }

    // ****************** GROUP MEMBERS ******************
    private val _groupMembers = MutableStateFlow<List<GroupMemberDataClass>>(emptyList())
    val groupMembers: StateFlow<List<GroupMemberDataClass>> = _groupMembers

    fun getMembersByGroupId(groupId: String) {
        viewModelScope.launch {
            repository.getMembersByGroupId(groupId).collect { members ->
                _groupMembers.value = members
            }
        }
    }

    fun insertMember(member: GroupMemberDataClass) {
        viewModelScope.launch {
            repository.insertMember(member)
//            fetchAllMembers()
        }
    }

    fun updateMember(member: GroupMemberDataClass) {
        viewModelScope.launch {
            repository.updateMember(member)
//            fetchAllMembers()
        }
    }

    fun deleteMember(userId: String,email: String) {
        viewModelScope.launch {
            repository.deleteMember(userId, email)
//            fetchAllMembers()
        }
    }

    fun deleteAllMembers(userId: String) {
        viewModelScope.launch {
            repository.deleteAllMembers(userId)
//            fetchAllMembers()
        }
    }

    // ****************** GROUP EXPENSES ******************
    private val _groupExpenses = MutableStateFlow<List<GroupExpenseDataClass>>(emptyList())
    val groupExpenses: StateFlow<List<GroupExpenseDataClass>> = _groupExpenses.asStateFlow()

    fun fetchAllExpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { expenses ->
                _groupExpenses.value = expenses
            }
        }
    }

    fun addExpense(expense: GroupExpenseDataClass) {
        viewModelScope.launch {
            repository.addExpense(expense)
            fetchAllExpenses()
        }
    }

    fun updateExpense(expense: GroupExpenseDataClass) {
        viewModelScope.launch {
            repository.updateExpense(expense)
            fetchAllExpenses()
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId)
            fetchAllExpenses()
        }
    }

    fun deleteAllExpenses() {
        viewModelScope.launch {
            repository.deleteAllExpenses()
            fetchAllExpenses()
        }
    }
}
