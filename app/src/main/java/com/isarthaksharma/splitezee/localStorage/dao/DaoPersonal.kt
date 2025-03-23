package com.isarthaksharma.splitezee.localStorage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoPersonal {
    @Query("SELECT * FROM PersonalDataClass ORDER BY expenseDate DESC")
    fun getAllPersonalExpense(): Flow<List<PersonalDataClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPersonalExpense(expense: PersonalDataClass)

    @Delete
    suspend fun removePersonalExpense(expense: PersonalDataClass)

    @Query("SELECT SUM(ExpenseAmt) FROM PersonalDataClass WHERE strftime('%Y-%m-%d', ExpenseDate / 1000, 'unixepoch') = strftime('%Y-%m-%d', 'now')")
    fun getPersonalTodayExpense(): Flow<Long?>

    @Query("SELECT Sum(ExpenseAmt) FROM PersonalDataClass WHERE strftime('%Y-%m', ExpenseDate / 1000, 'unixepoch') = strftime('%Y-%m', 'now')")
    fun getPersonalMonthExpense(): Flow<Long?>

    @Query("Select Sum(ExpenseAmt) FROM PersonalDataClass ")
    fun getPersonalAllExpense(): Flow<Long?>

    @Query("Delete from PersonalDataClass")
    suspend fun clearUserData()

    @Update
    suspend fun updateExpense(expense: PersonalDataClass)

    @Query("SELECT * FROM PersonalDataClass WHERE expenseId = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: String): PersonalDataClass?
}