package com.isarthaksharma.splitezee.localStorage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoGroup {
    @Query("SELECT * FROM GroupDataClass WHERE id = :groupId LIMIT 1")
    suspend fun getGroupById(groupId: Int): GroupDataClass?

    @Query("SELECT * FROM GroupDataClass ORDER BY groupCreationData DESC")
    fun getAllGroup(): Flow<List<GroupDataClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createGroup(groupInfo: GroupDataClass)

    @Delete
    suspend fun deleteGroup(groupInfo: GroupDataClass)

    @Update
    suspend fun updateGroup(groupInfo: GroupDataClass)

    @Query("UPDATE GroupDataClass SET groupMembers = :members WHERE id = :groupId")
    suspend fun addMemberToGroup(groupId: Int, members: List<String>)
}