package com.example.kotlinprogectapp.data.repository


import com.example.kotlinprogectapp.data.remote.api.ApiService
import com.example.kotlinprogectapp.data.remote.dto.*
import com.example.kotlinprogectapp.domain.model.*
import com.example.kotlinprogectapp.domain.repository.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val api: ApiService
) : FriendRepository {

    override suspend fun getFriends(): Result<List<Friend>> {
        return try {
            val dtos = api.getFriends()
            Result.success(dtos.map { it.toFriend(UserRelation.FRIEND) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFriendRequests(): Result<FriendRequests> {
        return try {
            val dto = api.getFriendRequests()
            Result.success(
                FriendRequests(
                    incoming = dto.incoming.map { it.toDomain(RequestDirection.INCOMING) },
                    outgoing = dto.outgoing.map { it.toDomain(RequestDirection.OUTGOING) }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendFriendRequest(userId: Long): Result<Unit> {
        return try {
            api.sendFriendRequest(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun respondToRequest(requestId: Long, accepted: Boolean): Result<Unit> {
        return try {
            api.respondToRequest(requestId, RespondDto(accepted))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFriend(userId: Long): Result<Unit> {
        return try {
            api.deleteFriend(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String): Result<List<Friend>> {
        return try {
            val dtos = api.searchUsers(query)
            Result.success(dtos.map { it.toFriend(UserRelation.NONE) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}