package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import com.isarthaksharma.splitezee.repository.AuthResponse
import com.isarthaksharma.splitezee.repository.AuthenticationUserRepository
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticateUserViewModel @Inject constructor(
    private val authenticationUserRepository: AuthenticationUserRepository
):ViewModel(){
    private val _authState = MutableStateFlow<AuthResponse>(AuthResponse.Loading)
    val authState: StateFlow<AuthResponse> = _authState

    fun signInWithGoogle(){
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading
            authenticationUserRepository.signInWithGoogle().collect{response ->
                _authState.value = response
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authenticationUserRepository.logout()
        }
    }

    private fun checkUserSession(){
        viewModelScope.launch {
            val currentUser = authenticationUserRepository.currentUser()
            _authState.value = if(currentUser != null) AuthResponse.Success else AuthResponse.Error("No Active Session")
        }
    }

    init{
        checkUserSession()
    }
}
