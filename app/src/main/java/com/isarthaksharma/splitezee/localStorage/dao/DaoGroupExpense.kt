package com.isarthaksharma.splitezee.localStorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoGroupExpense {

    // Get all expense
    @Query("SELECT * FROM GroupExpenseDataClass")
    fun getAllExpense(): Flow<List<GroupExpenseDataClass>>

    // Add Expense
    @Insert
    suspend fun addExpense(expense: GroupExpenseDataClass)

    // Delete Expense
    @Query("DELETE FROM GroupExpenseDataClass")
    suspend fun deleteAllExpense()

    // Delete a specific member by userId
    @Query("DELETE FROM GroupExpenseDataClass WHERE expenseId = :expenseId")
    suspend fun deleteMember(expenseId: String)

    // Update the existing Expense
    @Update
    suspend fun updateGroupExpense(expense: GroupExpenseDataClass)

    // Get particular expense
    @Query("SELECT * FROM GroupExpenseDataClass WHERE expenseId = :groupExpenseId LIMIT 1")
    suspend fun getGroupExpenseById(groupExpenseId: String): GroupExpenseDataClass?


}