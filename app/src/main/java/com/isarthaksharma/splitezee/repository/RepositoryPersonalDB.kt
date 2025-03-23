package com.isarthaksharma.splitezee.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.localStorage.dao.DaoPersonal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryPersonalDB @Inject constructor(
    private val dao: DaoPersonal,
    private val firestore: FirebaseFirestore
) {
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

    suspend fun updatePersonalExpense(expense: PersonalDataClass) {
        dao.updateExpense(expense)
    }

    suspend fun syncExpensesFromFireStore(userId: String) {
        val firestoreRef = firestore.collection("users")
            .document(userId)
            .collection("expenses")

        try {
            val snapshot = firestoreRef.get().await()

            val expensesFromFireStore = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PersonalDataClass::class.java)
            }

            for (expense in expensesFromFireStore) {
                val localExpense = dao.getExpenseById(expense.expenseId)
                if (localExpense == null) {
                    dao.addPersonalExpense(expense)
                }
            }

            Log.d("Sync", "Expenses synced successfully from FireStore")

        } catch (e: Exception) {
            Log.e("Sync", "Error syncing expenses: ${e.message}")
        }
    }
}