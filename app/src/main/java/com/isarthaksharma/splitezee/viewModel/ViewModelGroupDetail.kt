package com.isarthaksharma.splitezee.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.repository.RepositoryGroupDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

}
