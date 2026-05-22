package com.example.kotlinprogectapp.data.remote.dto


import com.example.kotlinprogectapp.domain.model.*

fun ChallengeDto.toDomain(): Challenge = Challenge(
    id          = id,
    title       = title,
    description = description,
    creatorId   = creatorId,
    creatorName = creatorName,
    deadline    = deadline,
    proofType   = ProofType.valueOf(proofType),
    status      = ChallengeStatus.valueOf(status),
    participantCount = participantCount,
    pendingProofs    = pendingProofs.map { it.toDomain() }
)

fun ProofDto.toDomain(): Proof = Proof(
    id        = id,
    userId    = userId,
    userName  = userName,
    mediaUrl  = mediaUrl,
    createdAt = createdAt
)

fun UserDto.toDomain(): User = User(
    id       = id,
    username = username,
    tag      = tag,
    email    = email
)

fun UserDto.toFriend(relation: UserRelation = UserRelation.NONE): Friend = Friend(
    id               = id,
    username         = username,
    tag              = tag,
    activeChallenges = 0,
    relation         = relation
)

fun UserDto.toStats(): UserStats = UserStats(
    created     = stats.created,
    completed   = stats.completed,
    successRate = stats.successRate
)

fun HistoryItemDto.toDomain(): ChallengeHistoryItem = ChallengeHistoryItem(
    title  = challengeTitle,
    result = ChallengeResult.valueOf(result),
    date   = date.toString()  // "2025-01-15"
)

fun FriendRequestDto.toDomain(direction: RequestDirection): FriendRequest = FriendRequest(
    id        = id,
    fromUser  = fromUser.toFriend(),
    direction = direction
)