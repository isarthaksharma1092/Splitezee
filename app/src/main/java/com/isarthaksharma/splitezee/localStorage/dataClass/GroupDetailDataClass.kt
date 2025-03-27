package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDetailDataClass(
    @PrimaryKey
    var groupDetailID: String = "",
    var groupName:String = "",
    var groupAdmin:String = "",
    val groupCreateDate: Long = 0L,
    var groupMemberDataClass: List<GroupMemberDataClass> = emptyList(),
    var totalExpense: Double = 0.0,
    var yourShare: Double = 0.0,
    var groupExpense: List<GroupExpenseDataClass> = emptyList()
)
//{
//    constructor() : this("","","",0L, emptyList<GroupMemberDataClass>(),0.0,0.0,emptyList<GroupExpenseDataClass>())
//}