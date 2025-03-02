package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.dataClass.userInfoFromGoogle
import com.isarthaksharma.splitezee.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val _userProfile = MutableStateFlow<userInfoFromGoogle?>(null)
    val userProfile: StateFlow<userInfoFromGoogle?> = _userProfile

    private fun loadUserProfile() {
        viewModelScope.launch {
            _userProfile.value = userInfoRepository.getUserInfo()
        }
    }

    init {
        loadUserProfile()
    }
}