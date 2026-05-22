package com.example.kotlinprogectapp.domain.usecase.friend

import com.example.kotlinprogectapp.domain.repository.FriendRepository
import javax.inject.Inject

class GetFriendRequestsUseCase @Inject constructor(private val repository: FriendRepository) {
    suspend operator fun invoke() = repository.getFriendRequests()
}