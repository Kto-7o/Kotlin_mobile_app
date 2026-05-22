package com.example.kotlinprogectapp.domain.model

data class User(
    val id: Long,
    val username: String,
    val tag: String,
    val email: String
)

data class UserStats(
    val created: Int,
    val completed: Int,
    val successRate: Int
)

data class ChallengeHistoryItem(
    val title: String,
    val result: ChallengeResult,
    val date: String
)

enum class ChallengeResult { SUCCESS, FAIL, EXPIRED }

data class RegisterData(
    val username: String,
    val tag: String,
    val email: String,
    val password: String
)