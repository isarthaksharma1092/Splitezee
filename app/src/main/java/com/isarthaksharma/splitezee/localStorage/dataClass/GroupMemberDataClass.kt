package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity

// For Firebase User ID and Information
@Entity(
    tableName = "group_members",
    primaryKeys = ["groupId", "email"]
)
data class GroupMemberDataClass(
    val groupId: String,
    val userId: String?,
    val email: String,
    val displayName: String,
    val profileImage: String?,
    val registered: Boolean
){
    constructor():this("","","","","",false)
}