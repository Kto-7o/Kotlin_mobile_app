package com.example.kotlinprogectapp.domain.usecase.challenge

import com.example.kotlinprogectapp.domain.repository.ChallengeRepository
import javax.inject.Inject

class SubmitVerdictUseCase @Inject constructor(
    private val repository: ChallengeRepository
) {
    suspend operator fun invoke(proofId: Long, accepted: Boolean) =
        repository.submitVerdict(proofId, accepted)
}