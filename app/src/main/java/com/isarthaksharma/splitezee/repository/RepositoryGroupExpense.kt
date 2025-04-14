package com.isarthaksharma.splitezee.repository

import com.isarthaksharma.splitezee.localStorage.dataBase.SplitezeeDatabase
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryGroupExpense @Inject constructor(
    private val db: SplitezeeDatabase
) {
    // ****************** GROUP EXPENSES ******************
    suspend fun isRoomEmpty(): Boolean {
        return db.daoGroupExpense().getExpenseCount() == 0
    }

    fun getAllExpenses(): Flow<List<GroupExpenseDataClass>> {
        return db.daoGroupExpense().getAllExpense()
    }

    suspend fun addExpense(expense: GroupExpenseDataClass) {
        db.daoGroupExpense().addExpense(expense)
    }

    suspend fun updateExpense(expense: GroupExpenseDataClass) {
        db.daoGroupExpense().updateGroupExpense(expense)
    }

    suspend fun deleteExpense(expenseId: String) {
        db.daoGroupExpense().deleteMember(expenseId)
    }

    suspend fun deleteAllExpenses(expenseId:String) {
        db.daoGroupExpense().deleteAllExpense(expenseId)
    }

    suspend fun getExpenseById(groupId: String): GroupExpenseDataClass? {
        return db.daoGroupExpense().getGroupExpenseById(groupId)
    }
}