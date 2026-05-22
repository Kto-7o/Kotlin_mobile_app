package com.example.kotlinprogectapp.domain.usecase.challenge

import com.example.kotlinprogectapp.domain.model.CreateChallengeData
import com.example.kotlinprogectapp.domain.repository.ChallengeRepository
import javax.inject.Inject

class CreateChallengeUseCase @Inject constructor(
    private val repository: ChallengeRepository
) {
    suspend operator fun invoke(data: CreateChallengeData): Result<*> {
        if (data.title.isBlank())
            return Result.failure<Nothing>(IllegalArgumentException("Название не может быть пустым"))
        if (data.description.isBlank())
            return Result.failure<Nothing>(IllegalArgumentException("Описание не может быть пустым"))
        if (data.invitedFriendIds.isEmpty())
            return Result.failure<Nothing>(IllegalArgumentException("Пригласите хотя бы одного друга"))
        return repository.createChallenge(data)
    }
}