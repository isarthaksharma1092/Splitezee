package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedUserInfoDataClass(
    @PrimaryKey (autoGenerate = true)
    val count:Int = 0,
    val savedUser_ID: String?,
    val savedUser_Name: String?,
    val savedUser_Profile: String?,
    val savedUser_Email: String?
)