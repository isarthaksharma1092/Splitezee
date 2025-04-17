package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "GroupExpenseDataClass",
    foreignKeys = [ForeignKey(
        entity = GroupDataClass::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("groupId")]
)
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