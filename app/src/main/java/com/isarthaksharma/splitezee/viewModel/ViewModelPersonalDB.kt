package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.repository.RepositoryPersonalDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPersonalDB @Inject constructor(private val repository: RepositoryPersonalDB) :
    ViewModel() {

    private val _expenses = MutableStateFlow<List<PersonalDataClass>>(emptyList())
    val expenses: StateFlow<List<PersonalDataClass>> = _expenses

    val personalTodayExpense: StateFlow<Long?> = repository.getPersonalTodayExpense()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)
    val personalMonthExpense: StateFlow<Long?> = repository.getPersonalMonthExpense()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)
    val personalTotalExpense: StateFlow<Long?> = repository.getPersonalAllExpense()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    init {
        viewModelScope.launch {
            repository.allExpenses.collect {
                _expenses.value = it
            }
        }
    }

    fun addPersonalExpense(expense: PersonalDataClass) {
        viewModelScope.launch {
            repository.addPersonalExpense(expense)
        }
    }

    fun removePersonalExpense(expense: PersonalDataClass) {
        viewModelScope.launch {
            repository.removePersonalExpense(expense)
        }
    }

    fun getPersonalTodayExpense() {
        viewModelScope.launch {
            repository.getPersonalTodayExpense()
        }
    }

    fun getPersonalMonthExpense() {
        viewModelScope.launch {
            repository.getPersonalMonthExpense()
        }
    }


    fun getPersonalAllExpense() {
        viewModelScope.launch {
            repository.getPersonalAllExpense()
        }
    }


    suspend fun cleanAllUserData() {
        viewModelScope.launch {
            repository.clearUserData()
        }
    }
}

