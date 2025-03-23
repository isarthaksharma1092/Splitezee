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
        val firestoreRef = firestore
            .collection("users")
            .document(userId)
            .collection("expenses")

        try {
            Log.d("Syncing", "Fetching expenses for user: $userId")
            val snapshot = firestoreRef.get().await()

            Log.d("Syncing", "Fetched ${snapshot.documents.size} documents from Firestore")

            if (snapshot.documents.isEmpty()) {
                Log.w("Syncing", "No expenses found in Firestore for this user.")
                return
            }

            val expensesFromFireStore = snapshot.documents.mapNotNull { doc ->
                Log.d("Syncing", "Firestore document: ${doc.data}") // ✅ Debug Firestore data
                doc.toObject(PersonalDataClass::class.java)
            }

            for (expense in expensesFromFireStore) {
                val localExpense = dao.getExpenseById(expense.expenseId)
                Log.d("Syncing", "Checking RoomDB for expenseId: ${expense.expenseId}, Found: $localExpense")

                if (localExpense == null) {
                    dao.addPersonalExpense(expense) // ✅ Insert missing expense
                    Log.d("Syncing", "Inserted new expense: ${expense.expenseId}")
                } else {
                    dao.updateExpense(expense) // ✅ Update existing expense
                    Log.d("Syncing", "Updated existing expense: ${expense.expenseId}")
                }
            }

            Log.d("Syncing", "Expenses synced successfully from Firestore")

        } catch (e: Exception) {
            Log.e("Syncing", "Error syncing expenses: ${e.message}", e)
        }
    }
}