package com.isarthaksharma.splitezee.localStorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoGroupMember {

    @Query("SELECT * FROM group_members WHERE groupId = :groupId")
    fun getMembersByGroupId(groupId: String): Flow<List<GroupMemberDataClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: GroupMemberDataClass)

    @Update
    suspend fun updateMembers(groupMember: GroupMemberDataClass)

    @Query("DELETE FROM group_members WHERE groupId = :groupId AND email = :email")
    suspend fun removeMember(groupId: String, email: String)

    @Query("DELETE FROM group_members WHERE groupId = :groupId")
    suspend fun deleteAllMembersFromGroup(groupId: String)
}
