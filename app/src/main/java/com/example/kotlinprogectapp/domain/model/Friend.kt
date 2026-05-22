package com.example.kotlinprogectapp.domain.model


data class Friend(
    val id: Long,
    val username: String,
    val tag: String,
    val activeChallenges: Int,
    val relation: UserRelation
)

enum class UserRelation { NONE, PENDING, FRIEND }

data class FriendRequest(
    val id: Long,
    val fromUser: Friend,
    val direction: RequestDirection
)

enum class RequestDirection { INCOMING, OUTGOING }

data class FriendRequests(
    val incoming: List<FriendRequest>,
    val outgoing: List<FriendRequest>
)