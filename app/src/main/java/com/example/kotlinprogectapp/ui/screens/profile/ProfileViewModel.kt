package com.example.kotlinprogectapp.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.model.UserRelation
import com.example.kotlinprogectapp.domain.usecase.auth.LogoutUseCase
import com.example.kotlinprogectapp.domain.usecase.friend.SendFriendRequestUseCase
import com.example.kotlinprogectapp.domain.usecase.user.*
import com.example.kotlinprogectapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle:          SavedStateHandle,
    private val getProfileUseCase:    GetProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserHistoryUseCase: GetCurrentUserIdUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val logoutUseCase:        LogoutUseCase
) : ViewModel() {

    // Читаем userId из аргументов навигации (null = свой профиль)
    private val userId: Long? = savedStateHandle.get<Long>("userId")

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    init {
        val currentUserId = getCurrentUserIdUseCase()
        val isOwn = userId == null || userId == currentUserId
        _uiState.update { it.copy(isOwnProfile = isOwn) }
        loadProfile()
    }

    fun onAddFriend() {
        val id = userId ?: return
        viewModelScope.launch {
            sendFriendRequestUseCase(id).onSuccess {
                // Оптимистичное обновление — не ждём перезагрузки
                _uiState.update { it.copy(relation = UserRelation.PENDING) }
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _events.send(Screen.Login.route)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val targetId = userId

            val profileJob = async { getProfileUseCase(targetId) }

            profileJob.await()
                .onSuccess { user ->
                    _uiState.update { it.copy(user = user, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
