package com.isarthaksharma.splitezee.repository

import com.isarthaksharma.splitezee.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.localStorage.DaoPersonal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryPersonalDB @Inject constructor(private val dao: DaoPersonal) {
    val allExpenses: Flow<List<PersonalDataClass>> = dao.getAllPersonalExpense()

    suspend fun addPersonalExpense(expense: PersonalDataClass) {
        dao.addPersonalExpense(expense)
    }

    suspend fun removePersonalExpense(expense: PersonalDataClass) {
        dao.removePersonalExpense(expense)
    }

    fun getPersonalTodayExpense():Flow<Long?> {
        return dao.getPersonalTodayExpense()
    }

    fun getPersonalMonthExpense() :Flow<Long?>{
        return dao.getPersonalMonthExpense()
    }

    fun getPersonalAllExpense() :Flow<Long?>{
        return dao.getPersonalAllExpense()
    }

    suspend fun clearUserData() {
        dao.clearUserData()
    }
}