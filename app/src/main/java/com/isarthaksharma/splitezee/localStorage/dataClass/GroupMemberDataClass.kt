package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

// For Firebase User ID and Information
@Entity
data class GroupMemberDataClass(
    @PrimaryKey
    val userId: String,
    val email: String,
    val displayName: String = "Anonymous",
    val profileImage: String?,
    val isRegistered: Boolean
)