package com.example.kotlinprogectapp.domain.model

import java.time.LocalDate

data class Challenge(
    val id: Long,
    val title: String,
    val description: String,
    val creatorId: Long,
    val creatorName: String,
    val deadline: LocalDate,
    val proofType: ProofType,
    val status: ChallengeStatus,
    val participantCount: Int,
    val pendingProofs: List<Proof> = emptyList()
)

enum class ChallengeStatus { ACTIVE, COMPLETED, EXPIRED, INCOMING }
enum class ProofType       { PHOTO, VIDEO, SCREENSHOT }
enum class FeedTab         { ACTIVE, INCOMING, ARCHIVE }

data class CreateChallengeData(
    val title: String,
    val description: String,
    val proofType: ProofType,
    val deadlineDays: Int,
    val invitedFriendIds: List<Long>
)