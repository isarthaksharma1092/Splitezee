package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class GroupExpenseDataClass(
    @PrimaryKey
    val expenseId: String = UUID.randomUUID().toString(),
    val paidBy: String,
    val amount: Double,
    val description: String,
    val date: Long
)