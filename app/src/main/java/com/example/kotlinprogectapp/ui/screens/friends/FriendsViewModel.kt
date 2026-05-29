package com.example.kotlinprogectapp.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.usecase.friend.*
import com.example.kotlinprogectapp.domain.usecase.user.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getRequestsUseCase: GetFriendRequestsUseCase,
    private val sendRequestUseCase: SendFriendRequestUseCase,
    private val respondToRequestUseCase: RespondToRequestUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init { loadAll() }

    fun onTabSelected(tab: FriendsTab) =
        _uiState.update { it.copy(activeTab = tab) }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.update { it.copy(isSearching = true) }
            searchUsersUseCase(query)
                .onSuccess { results ->
                    _uiState.update { it.copy(searchResults = results, isSearching = false) }
                }
                .onFailure {
                    // при ошибке просто сбрасываем флаг, не показываем ошибку
                    _uiState.update { it.copy(isSearching = false, searchResults = emptyList()) }
                }
        }
    }

    fun onSendRequest(userId: Long) {
        viewModelScope.launch {
            sendRequestUseCase(userId).onSuccess {
                _uiState.update { state ->
                    state.copy(searchResults = state.searchResults.map {
                        if (it.id == userId)
                            it.copy(relation = com.example.kotlinprogectapp.domain.model.UserRelation.PENDING)
                        else it
                    })
                }
            }
        }
    }

    fun onRequestRespond(requestId: Long, accepted: Boolean) {
        viewModelScope.launch {
            respondToRequestUseCase(requestId, accepted).onSuccess { loadAll() }
        }
    }

    fun onDeleteFriend(userId: Long) {
        viewModelScope.launch {
            deleteFriendUseCase(userId).onSuccess { loadAll() }
        }
    }

    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val friendsJob  = async { getFriendsUseCase() }
            val requestsJob = async { getRequestsUseCase() }

            val friends  = friendsJob.await()
            val requests = requestsJob.await()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    friends = friends.getOrDefault(emptyList()),
                    incomingRequests = requests.getOrNull()?.incoming ?: emptyList(),
                    outgoingRequests = requests.getOrNull()?.outgoing ?: emptyList()
                )
            }
        }
    }
}