package com.example.kotlinprogectapp.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.model.Friend
import com.example.kotlinprogectapp.domain.model.ProofType
import com.example.kotlinprogectapp.domain.usecase.challenge.CreateChallengeUseCase
import com.example.kotlinprogectapp.domain.usecase.friend.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val createChallengeUseCase: CreateChallengeUseCase,
    private val getFriendsUseCase:      GetFriendsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateChallengeUiState())
    val uiState: StateFlow<CreateChallengeUiState> = _uiState.asStateFlow()

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    init { loadFriends() }

    fun onTitleChanged(value: String) =
        _uiState.update { it.copy(title = value, error = null) }

    fun onDescriptionChanged(value: String) =
        _uiState.update { it.copy(description = value) }

    fun onProofTypeChanged(type: ProofType) =
        _uiState.update { it.copy(proofType = type) }

    fun onDeadlineChanged(days: Int) =
        _uiState.update { it.copy(deadlineDays = days) }

    fun onFriendToggled(friendId: Long) {
        _uiState.update { state ->
            val updated = if (friendId in state.invitedFriends)
                state.invitedFriends - friendId
            else
                state.invitedFriends + friendId
            state.copy(invitedFriends = updated)
        }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (!state.isValid) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            createChallengeUseCase(state.toDomainData())
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun loadFriends() {
        viewModelScope.launch {
            getFriendsUseCase().onSuccess { _friends.value = it }
        }
    }
}
