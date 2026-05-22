package com.example.kotlinprogectapp.data.local.entity


import com.example.kotlinprogectapp.data.remote.dto.ChallengeDto
import com.example.kotlinprogectapp.data.remote.dto.UserDto
import com.example.kotlinprogectapp.domain.model.FeedTab

fun ChallengeDto.toEntity(tab: FeedTab): ChallengeEntity = ChallengeEntity(
    id               = id,
    title            = title,
    description      = description,
    creatorName      = creatorName,
    deadline         = deadline.toString(),
    proofType        = proofType,
    status           = status,
    participantCount = participantCount,
    tab              = tab.name.lowercase()
)

fun UserDto.toEntity(): UserEntity = UserEntity(
    id       = id,
    username = username,
    tag      = tag,
    email    = email
)