package com.isarthaksharma.splitezee.repository

sealed interface AuthResponse {
    data object Success: AuthResponse
    object Loading: AuthResponse
    data class Error(val message: String): AuthResponse
}