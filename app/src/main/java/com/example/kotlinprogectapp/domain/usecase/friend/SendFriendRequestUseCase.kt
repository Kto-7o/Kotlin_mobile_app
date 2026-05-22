package com.example.kotlinprogectapp.domain.usecase.friend

import com.example.kotlinprogectapp.domain.repository.FriendRepository
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(private val repository: FriendRepository) {
    suspend operator fun invoke(userId: Long) = repository.sendFriendRequest(userId)
}