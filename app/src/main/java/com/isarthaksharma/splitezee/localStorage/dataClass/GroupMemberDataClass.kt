package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// For Firebase User ID and Information

@Entity(
    tableName = "group_members",
    primaryKeys = ["groupId", "email"],
    foreignKeys = [ForeignKey(
        entity = GroupDataClass::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("groupId")]
)
data class GroupMemberDataClass(
    val groupId: String,
    val userId: String?,
    val email: String,
    val displayName: String,
    val profileImage: String?,
    val registered: Boolean
) {
    constructor() : this("", "", "", "", "", false)
}