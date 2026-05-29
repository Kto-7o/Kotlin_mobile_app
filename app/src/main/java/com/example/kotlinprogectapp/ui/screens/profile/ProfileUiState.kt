package com.example.kotlinprogectapp.ui.screens.profile

import com.example.kotlinprogectapp.domain.model.*

data class ProfileUiState(
    val user: User? = null,
    val stats: UserStats? = null,
    val history: List<ChallengeHistoryItem> = emptyList(),
    val isOwnProfile: Boolean = true,
    val relation: UserRelation = UserRelation.NONE,
    val isLoading: Boolean = false,
    val error: String? = null
)
