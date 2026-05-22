package com.example.kotlinprogectapp.domain.usecase.friend

import com.example.kotlinprogectapp.domain.repository.FriendRepository
import javax.inject.Inject

class RespondToRequestUseCase @Inject constructor(private val repository: FriendRepository) {
    suspend operator fun invoke(requestId: Long, accepted: Boolean) =
        repository.respondToRequest(requestId, accepted)
}