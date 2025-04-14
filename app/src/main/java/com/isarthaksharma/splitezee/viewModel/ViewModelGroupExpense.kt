package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.repository.RepositoryGroupExpense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroupExpense@Inject constructor(
    private val repository: RepositoryGroupExpense)
    :ViewModel() {
    // ****************** GROUP EXPENSES ******************
    private val _groupExpenses = MutableStateFlow<List<GroupExpenseDataClass>>(emptyList())
    val groupExpenses: StateFlow<List<GroupExpenseDataClass>> = _groupExpenses.asStateFlow()

    private val _isDatabaseEmpty = MutableLiveData<Boolean>()
    val isDatabaseEmpty: LiveData<Boolean> get() = _isDatabaseEmpty

    fun checkIfDatabaseIsEmpty() {
        viewModelScope.launch {
            val rowCount = repository.isRoomEmpty()
            _isDatabaseEmpty.postValue(rowCount == true)
        }
    }

    private fun fetchAllExpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { expenses ->
                _groupExpenses.value = expenses
            }
        }
    }

    fun addExpense(expense: GroupExpenseDataClass) {
        viewModelScope.launch {
            repository.addExpense(expense)
            fetchAllExpenses()
        }
    }

    fun updateExpense(expense: GroupExpenseDataClass) {
        viewModelScope.launch {
            repository.updateExpense(expense)
            fetchAllExpenses()
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId)
            fetchAllExpenses()
        }
    }

    fun deleteAllExpenses(expenseId:String) {
        viewModelScope.launch {
            repository.deleteAllExpenses(expenseId)
            fetchAllExpenses()
        }
    }

    fun getExpenseById(groupId: String){
        viewModelScope.launch {
            repository.getExpenseById(groupId)
        }
    }
}