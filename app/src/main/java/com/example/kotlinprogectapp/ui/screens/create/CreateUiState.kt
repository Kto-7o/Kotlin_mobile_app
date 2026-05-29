package com.example.kotlinprogectapp.ui.screens.create

import com.example.kotlinprogectapp.domain.model.CreateChallengeData
import com.example.kotlinprogectapp.domain.model.ProofType

data class CreateChallengeUiState(
    val title: String = "",
    val description: String = "",
    val proofType: ProofType = ProofType.PHOTO,
    val deadlineDays: Int = 7,
    val invitedFriends: List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

val CreateChallengeUiState.isValid: Boolean
    get() = title.isNotBlank() && description.isNotBlank()// && invitedFriends.isNotEmpty()

fun CreateChallengeUiState.toDomainData() = CreateChallengeData(
    title = title,
    description = description,
    proofType = proofType,
    deadlineDays = deadlineDays,
    invitedFriendIds = invitedFriends
)
