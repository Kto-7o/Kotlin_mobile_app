package com.example.kotlinprogectapp.domain.usecase.challenge

import com.example.kotlinprogectapp.domain.repository.ChallengeRepository
import java.io.File
import javax.inject.Inject

class UploadProofUseCase @Inject constructor(
    private val repository: ChallengeRepository
) {
    suspend operator fun invoke(challengeId: Long, file: File) =
        repository.uploadProof(challengeId, file)
}