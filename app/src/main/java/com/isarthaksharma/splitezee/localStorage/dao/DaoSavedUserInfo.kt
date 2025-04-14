package com.isarthaksharma.splitezee.localStorage.dao;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.isarthaksharma.splitezee.localStorage.dataClass.SavedUserInfoDataClass;

@Dao
interface DaoSavedUserInfo {
    // Check if email exits or not
    @Query("SELECT EXISTS(SELECT 1 FROM SavedUserInfoDataClass WHERE savedUser_Email = :email)")
    suspend fun isUserEmailExists(email: String): Boolean

    // Get the email info
    @Query("SELECT * FROM SavedUserInfoDataClass WHERE savedUser_Email = :email LIMIT 1")
    suspend fun getUserInfoByEmail(email: String): SavedUserInfoDataClass

    // Insert into the DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(savedUserInfoDataClass: SavedUserInfoDataClass)
}