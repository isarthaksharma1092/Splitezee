package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.localStorage.dataClass.SavedUserInfoDataClass
import com.isarthaksharma.splitezee.repository.RepositorySaveUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSaveUserInfo @Inject constructor(private val repositorySaveUserInfo: RepositorySaveUserInfo):ViewModel() {

    fun checkIfUserExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repositorySaveUserInfo.checkUserInfoExist(email)
            onResult(exists)
        }
    }
    fun insertSavedUserInfo(savedUserInfoDataClass: SavedUserInfoDataClass){
        viewModelScope.launch{
            repositorySaveUserInfo.insertUser(savedUserInfoDataClass)
        }
    }

    fun getSavedUserInfo(email:String, onResult: (SavedUserInfoDataClass) -> Unit){
        viewModelScope.launch {
            val user = repositorySaveUserInfo.getUser(email)
            onResult(user)
        }
    }
}