package com.isarthaksharma.splitezee.localStorage.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expenseName: String,
    val expenseAmt: Double,
    val expenseMsg: String?,
    val expenseDate: Long,
    val expenseCurrency: String
)
