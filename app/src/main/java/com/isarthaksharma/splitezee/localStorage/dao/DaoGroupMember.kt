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
    // Fetch All group Member
    @Query("SELECT * FROM GroupMemberDataClass")
    fun getAllMembers(): Flow<List<GroupMemberDataClass>>

    // Update Info of Existing User
    @Update
    suspend fun updateMembers(groupMember: GroupMemberDataClass)

    // Add Members
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupMemberDataClass)

    // Delete Members
    @Query("DELETE FROM GroupMemberDataClass WHERE userId = :userId")
    suspend fun deleteMember(userId: String)

    // Delete All Member (Only in case of Group Delete)
    @Query("DELETE FROM GroupMemberDataClass")
    suspend fun deleteAllGroups()
}