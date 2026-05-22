package com.example.kotlinprogectapp.domain.repository

import com.example.kotlinprogectapp.domain.model.*

interface FriendRepository {
    suspend fun getFriends(): Result<List<Friend>>
    suspend fun getFriendRequests(): Result<FriendRequests>
    suspend fun sendFriendRequest(userId: Long): Result<Unit>
    suspend fun respondToRequest(requestId: Long, accepted: Boolean): Result<Unit>
    suspend fun deleteFriend(userId: Long): Result<Unit>
    suspend fun searchUsers(query: String): Result<List<Friend>>
}