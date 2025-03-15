package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import com.isarthaksharma.splitezee.repository.RepositoryGroupDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroupDB @Inject constructor(private val repositoryGroupDB: RepositoryGroupDB) : ViewModel() {

    private val _selectedGroup = MutableLiveData<GroupDataClass?>()
    val selectedGroup: LiveData<GroupDataClass?> = _selectedGroup

    fun setSelectedGroup(group: GroupDataClass) {
        _selectedGroup.value = group
    }

    private var _group = MutableStateFlow<List<GroupDataClass>>(emptyList())
    val group: StateFlow<List<GroupDataClass>> = _group

    init {
        viewModelScope.launch {
            repositoryGroupDB.getAllGroup().collect { newGroups ->
                _group.update { newGroups }
            }
        }
    }

    fun addMember(groupId: Int, memberNameList: List<String>) {
        viewModelScope.launch {
            repositoryGroupDB.addMemberToGroup(groupId, memberNameList)
        }
    }

    fun createGroup(groupInfo: GroupDataClass) {
        viewModelScope.launch {
            repositoryGroupDB.createGroup(groupInfo)
        }
    }

    fun deleteGroup(groupInfo: GroupDataClass) {
        viewModelScope.launch {
            repositoryGroupDB.deleteGroup(groupInfo)
        }
    }

}