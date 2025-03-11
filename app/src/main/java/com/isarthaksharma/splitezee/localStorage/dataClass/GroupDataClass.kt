package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val groupName: String,
    val groupAdmin:String,
    val groupCreationData:Long,
    val groupMembers:List<String>,
    val syncStatus: Boolean = false
)