package com.example.kotlinprogectapp.data.remote.dto


import java.time.LocalDate

data class LoginDto(
    val login: String,
    val password: String
)

data class RegisterDto(
    val username: String,
    val tag: String,
    val email: String,
    val password: String
)

data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshDto(
    val refreshToken: String
)

data class TagCheckDto(
    val available: Boolean
)

data class StatsDto(
    val created: Int,
    val completed: Int,
    val successRate: Int
)

data class HistoryItemDto(
    val challengeTitle: String,
    val result: String,       // "SUCCESS" | "FAIL" | "EXPIRED"
    val date: LocalDate
)

data class UserDto(
    val id: Long,
    val username: String,
    val tag: String,
    val email: String,
    val stats: StatsDto,
    val history: List<HistoryItemDto> = emptyList()
)

data class ProofDto(
    val id: Long,
    val userId: Long,
    val userName: String,
    val mediaUrl: String,
    val createdAt: LocalDate
)

data class ChallengeDto(
    val id: Long,
    val title: String,
    val description: String,
    val creatorId: Long,
    val creatorName: String,
    val deadline: LocalDate,
    val proofType: String,    // "PHOTO" | "VIDEO" | "SCREENSHOT"
    val status: String,       // "ACTIVE" | "COMPLETED" | "EXPIRED" | "INCOMING"
    val participantCount: Int,
    val pendingProofs: List<ProofDto> = emptyList()
)

data class CreateChallengeDto(
    val title: String,
    val description: String,
    val proofType: String,
    val deadlineDays: Int,
    val invitedUserIds: List<Long>
)

data class VerdictDto(
    val proofId: Long,
    val accepted: Boolean
)

data class FriendRequestDto(
    val id: Long,
    val fromUser: UserDto
)

data class FriendRequestsDto(
    val incoming: List<FriendRequestDto>,
    val outgoing: List<FriendRequestDto>
)

data class RespondDto(
    val accepted: Boolean
)