package com.isarthaksharma.splitezee.repository

import com.isarthaksharma.splitezee.localStorage.dao.DaoGroup
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryGroupDB @Inject constructor(private val daoGroup: DaoGroup){

    fun getAllGroup():Flow<List<GroupDataClass>>{
        return daoGroup.getAllGroup()
    }

    suspend fun createGroup(groupInfo:GroupDataClass){
        return daoGroup.createGroup(groupInfo)
    }

    suspend fun deleteGroup(groupInfo: GroupDataClass){
        return daoGroup.deleteGroup(groupInfo)
    }

    suspend fun addMemberToGroup(groupId:Int,newMembers:List<String>){
        val group = daoGroup.getGroupById(groupId) ?: return
        val updatedMembers = group.groupMembers.toMutableList().apply { addAll(newMembers) }
        val updatedGroup = group.copy(groupMembers = updatedMembers)
        daoGroup.updateGroup(updatedGroup)
    }
}