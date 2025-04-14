package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupExpenseDataClass(
    @PrimaryKey
    val expenseId: String,
    val groupId: String,
    val addedBy: String,
    val expenseTitle: String,
    val totalAmount: String,
    val date: Long,
    val splitAmong: List<GroupMemberDataClass> = emptyList(),
    val paidShares: Map<String, Double> = emptyMap(),
    val owedShares: Map<String, Double> = emptyMap()
){
    constructor():this("","","","","",0L)
}