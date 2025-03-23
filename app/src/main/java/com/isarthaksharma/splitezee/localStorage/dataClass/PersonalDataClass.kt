package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalDataClass(
    @PrimaryKey val expenseId: String,
    val expenseName: String,
    val expenseAmt: Double,
    val expenseMsg: String?,
    val expenseDate: Long,
    val expenseCurrency: String
)
