package com.example.kotlinprogectapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.usecase.auth.LoginUseCase
import com.example.kotlinprogectapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    fun onLoginChanged(value: String) =
        _uiState.update { it.copy(login = value, error = null) }

    fun onPasswordChanged(value: String) =
        _uiState.update { it.copy(password = value, error = null) }

    fun onPasswordVisibilityToggle() =
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onSubmit() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(state.login, state.password)
                .onSuccess { _events.send(Screen.Feed.route) }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка входа")
                    }
                }
        }
    }
}