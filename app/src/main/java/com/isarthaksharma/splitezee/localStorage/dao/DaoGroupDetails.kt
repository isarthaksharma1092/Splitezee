package com.isarthaksharma.splitezee.localStorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoGroupDetails {

    // Get Particular Group Details
    @Query("SELECT * FROM GroupDetailDataClass WHERE groupDetailID = :groupId")
    fun getGroupDetails(groupId: String): Flow<GroupDetailDataClass?>

    // Insert into GroupDetail DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupDetailDataClass)

    // Update Existing group details
    @Update
    suspend fun updateGroup(group: GroupDetailDataClass)

    // Delete the specific Group only ADMIN
    @Query("DELETE FROM GroupDetailDataClass where groupDetailID = :groupDetailID")
    suspend fun deleteByGroupID(groupDetailID: String)

    // Delete the Group DB [For Local Caching]
    @Query("DELETE FROM GroupDetailDataClass")
    suspend fun deleteAllGroups()
}