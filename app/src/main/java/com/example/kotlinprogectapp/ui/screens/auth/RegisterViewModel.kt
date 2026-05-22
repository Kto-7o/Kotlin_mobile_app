package com.example.kotlinprogectapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.model.RegisterData
import com.example.kotlinprogectapp.domain.usecase.auth.CheckTagUseCase
import com.example.kotlinprogectapp.domain.usecase.auth.RegisterUseCase
import com.example.kotlinprogectapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val checkTagUseCase: CheckTagUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    private var tagCheckJob: Job? = null

    fun onUsernameChanged(value: String) =
        _uiState.update { it.copy(username = value) }

    fun onTagChanged(value: String) {
        _uiState.update { it.copy(tag = value, tagAvailable = null) }

        if (value.isBlank()) return

        tagCheckJob?.cancel()
        tagCheckJob = viewModelScope.launch {
            delay(500)
            _uiState.update { it.copy(isCheckingTag = true) }
            checkTagUseCase(value)
                .onSuccess { available ->
                    _uiState.update { it.copy(tagAvailable = available, isCheckingTag = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isCheckingTag = false) }
                }
        }
    }

    fun onEmailChanged(value: String) =
        _uiState.update { it.copy(email = value) }

    fun onPasswordChanged(value: String) =
        _uiState.update { it.copy(password = value) }

    fun onSubmit() {
        val state = _uiState.value
        if (!state.isValid) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            registerUseCase(
                RegisterData(state.username, state.tag, state.email, state.password)
            )
                .onSuccess { _events.send(Screen.Feed.route) }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка регистрации")
                    }
                }
        }
    }
}