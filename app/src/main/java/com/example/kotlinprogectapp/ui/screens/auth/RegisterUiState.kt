package com.example.kotlinprogectapp.ui.screens.auth

data class RegisterUiState(
    val username: String = "",
    val tag: String = "",
    val email: String = "",
    val password: String = "",
    val tagAvailable: Boolean? = null,
    val isCheckingTag: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

val RegisterUiState.isValid: Boolean
    get() = username.isNotBlank()
            && tag.isNotBlank()
            && email.isNotBlank()
            && password.isNotBlank()
            && tagAvailable == true
