package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.repository.RepositoryFireStoreUpload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelFireStoreUpload @Inject constructor(private val repositoryFireStoreUpload: RepositoryFireStoreUpload): ViewModel(){
    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists:StateFlow<Boolean?> = _emailExists

    fun checkEmail(email: String) {
        viewModelScope.launch {
            _emailExists.emit(repositoryFireStoreUpload.checkIfEmailExists(email))
        }
    }

    fun uploadPersonalExpense(expense:PersonalDataClass){
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repositoryFireStoreUpload.uploadPersonalExpense(
                userId = userId,
                expense = expense
            )
        }
    }
}