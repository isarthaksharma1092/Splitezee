package com.isarthaksharma.splitezee.repository

import com.isarthaksharma.splitezee.localStorage.dataBase.SplitezeeDatabase
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryGroupDetail @Inject constructor(
    private val db: SplitezeeDatabase
) {
    // ****************** GROUP DETAILS ******************

    fun getGroupDetailById(groupDetailID: String): Flow<GroupDetailDataClass> {
        return db.daoGroupDetails().getGroupDetails(groupDetailID)
    }

    suspend fun insertGroup(group: GroupDetailDataClass) {
        db.daoGroupDetails().insertGroup(group)
    }

    suspend fun updateGroup(group: GroupDetailDataClass) {
        db.daoGroupDetails().updateGroup(group)
    }

    suspend fun deleteGroup(groupId: String) {
        db.daoGroupDetails().deleteByGroupID(groupId)
    }

    suspend fun deleteAllGroups() {
        db.daoGroupDetails().deleteAllGroups()
    }

    // ****************** GROUP MEMBERS ******************

    fun getMembersByGroupId(groupId: String): Flow<List<GroupMemberDataClass>> {
        return db.daoGroupMember().getMembersByGroupId(groupId)
    }

    suspend fun insertMember(member: GroupMemberDataClass) {
        db.daoGroupMember().insertMember(member)
    }

    suspend fun updateMember(member: GroupMemberDataClass) {
        db.daoGroupMember().updateMembers(member)
    }

    suspend fun deleteMember(userId: String, email:String) {
        db.daoGroupMember().removeMember(userId, email)
    }

    suspend fun deleteAllMembers(userId:String) {
        db.daoGroupMember().deleteAllMembersFromGroup(userId)
    }

}