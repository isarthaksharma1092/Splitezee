package com.isarthaksharma.splitezee.repository

import android.content.Context
import com.isarthaksharma.splitezee.localStorage.dataBase.SplitezeeDatabase
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryGroupDetail @Inject constructor(
    private val db: SplitezeeDatabase
) {
    // ****************** GROUP DETAILS ******************

    fun getGroupDetailById(groupDetailID: String): Flow<GroupDetailDataClass?> {
        return db.daoGroupDetails().getGroupDetails(groupDetailID)
    }

    suspend fun insertGroup(group: GroupDetailDataClass) {
        db.daoGroupDetails().insertGroup(group)
    }

    suspend fun updateGroup(group: GroupDetailDataClass) {
        db.daoGroupDetails().updateGroup(group)
    }

    suspend fun deleteGroup(groupId: String) {
        db.daoGroupDetails().deleteByGroupID(groupId)
    }

    suspend fun deleteAllGroups() {
        db.daoGroupDetails().deleteAllGroups()
    }

    // ****************** GROUP MEMBERS ******************

    fun getAllMembers(): Flow<List<GroupMemberDataClass>> {
        return db.daoGroupMember().getAllMembers()
    }

    suspend fun insertMember(member: GroupMemberDataClass) {
        db.daoGroupMember().insertGroup(member)
    }

    suspend fun updateMember(member: GroupMemberDataClass) {
        db.daoGroupMember().updateMembers(member)
    }

    suspend fun deleteMember(userId: String) {
        db.daoGroupMember().deleteMember(userId)
    }

    suspend fun deleteAllMembers() {
        db.daoGroupMember().deleteAllGroups()
    }

    // ****************** GROUP EXPENSES ******************

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

    suspend fun deleteAllExpenses() {
        db.daoGroupExpense().deleteAllExpense()
    }

    suspend fun getExpenseById(expenseId: String): GroupExpenseDataClass? {
        return db.daoGroupExpense().getGroupExpenseById(expenseId)
    }
}