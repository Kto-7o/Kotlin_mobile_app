package com.example.kotlinprogectapp.ui.screens.feed

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.model.FeedTab
import com.example.kotlinprogectapp.domain.usecase.challenge.GetChallengesUseCase
import com.example.kotlinprogectapp.domain.usecase.challenge.SubmitVerdictUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getChallengesUseCase: GetChallengesUseCase,
    private val submitVerdictUseCase: SubmitVerdictUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init { loadChallenges() }

    fun onTabSelected(tab: FeedTab) {
        _uiState.update { it.copy(activeTab = tab, challenges = emptyList()) }
        loadChallenges()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRefresh() = loadChallenges()

    fun onVerdictSubmit(proofId: Long, accepted: Boolean) {
        viewModelScope.launch {
            submitVerdictUseCase(proofId, accepted).onSuccess {
                _uiState.update { state ->
                    state.copy(challenges = state.challenges.map { challenge ->
                        challenge.copy(
                            pendingProofs = challenge.pendingProofs
                                .filter { it.id != proofId }
                        )
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadChallenges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getChallengesUseCase(_uiState.value.activeTab)
                .onSuccess { challenges ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading  = false,
                            challenges = challenges.map { challenge ->
                                ChallengeUi(
                                    id = challenge.id,
                                    title = challenge.title,
                                    creatorName = challenge.creatorName,
                                    deadlineLabel = formatDeadline(challenge.deadline),
                                    participantCount = challenge.participantCount,
                                    status = challenge.status,
                                    proofType = challenge.proofType,
                                    pendingProofs = challenge.pendingProofs.map { proof ->
                                        ProofUi(
                                            id = proof.id,
                                            userId = proof.userId,
                                            userName = proof.userName,
                                            mediaUrl = proof.mediaUrl,
                                            challengeTitle = challenge.title
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}