package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalDataClass(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,
    val firestoreExpenseId: String = "",
    val expenseName: String,
    val expenseAmt: Double,
    val expenseMsg: String?,
    val expenseDate: Long,
    val expenseCurrency: String
)
