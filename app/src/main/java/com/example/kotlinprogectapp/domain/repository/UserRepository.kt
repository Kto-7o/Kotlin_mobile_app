package com.example.kotlinprogectapp.domain.repository

import com.example.kotlinprogectapp.domain.model.*

interface UserRepository {
    suspend fun getMe(): Result<User>
    suspend fun getUserById(id: Long): Result<User>
    suspend fun searchUsers(query: String): Result<List<Friend>>
    fun getCurrentUserId(): Long?
    suspend fun getUserHistory(userId: Long): Result<List<ChallengeHistoryItem>>
    suspend fun getUserStats(userId: Long): Result<UserStats>
}