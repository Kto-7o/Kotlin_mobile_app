package com.example.kotlinprogectapp.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinprogectapp.domain.model.*
import java.time.LocalDate


@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val creatorName: String,
    val deadline: String,          // "2025-01-15"
    val proofType: String,
    val status: String,
    val participantCount: Int,
    val tab: String                // "active" | "incoming" | "archive"
)

fun ChallengeEntity.toDomain(): Challenge = Challenge(
    id               = id,
    title            = title,
    description      = description,
    creatorId        = 0L,
    creatorName      = creatorName,
    deadline         = LocalDate.parse(deadline),
    proofType        = ProofType.valueOf(proofType),
    status           = ChallengeStatus.valueOf(status),
    participantCount = participantCount,
    pendingProofs    = emptyList()
)