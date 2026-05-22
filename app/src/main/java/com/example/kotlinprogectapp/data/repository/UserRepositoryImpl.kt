package com.example.kotlinprogectapp.data.repository


import com.example.kotlinprogectapp.data.local.dao.UserDao
import com.example.kotlinprogectapp.data.local.entity.toEntity
import com.example.kotlinprogectapp.data.local.entity.toDomain
import com.example.kotlinprogectapp.data.remote.api.ApiService
import com.example.kotlinprogectapp.data.remote.dto.*
import com.example.kotlinprogectapp.data.storage.TokenStorage
import com.example.kotlinprogectapp.domain.model.*
import com.example.kotlinprogectapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: UserDao,
    private val tokenStorage: TokenStorage
) : UserRepository {

    override suspend fun getMe(): Result<User> {
        return try {
            val dto = api.getMe()
            dao.insert(dto.toEntity())
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            val id = tokenStorage.getCurrentUserId() ?: return Result.failure(e)
            val cached = dao.getUserById(id)
            if (cached != null) Result.success(cached.toDomain())
            else Result.failure(e)
        }
    }

    override suspend fun getUserById(id: Long): Result<User> {
        return try {
            val dto = api.getProfile(id)
            dao.insert(dto.toEntity())
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            val cached = dao.getUserById(id)
            if (cached != null) Result.success(cached.toDomain())
            else Result.failure(e)
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

    override fun getCurrentUserId(): Long? = tokenStorage.getCurrentUserId()

    override suspend fun getUserHistory(userId: Long): Result<List<ChallengeHistoryItem>> {
        return try {
            val dto = api.getProfile(userId)
            Result.success(dto.history.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserStats(userId: Long): Result<UserStats> {
        return try {
            val dto = api.getProfile(userId)
            Result.success(dto.toStats())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}