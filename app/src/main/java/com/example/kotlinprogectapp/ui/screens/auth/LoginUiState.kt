package com.example.kotlinprogectapp.ui.screens.auth

data class LoginUiState(
    val login:           String  = "",
    val password:        String  = "",
    val passwordVisible: Boolean = false,
    val isLoading:       Boolean = false,
    val error:           String? = null
)
