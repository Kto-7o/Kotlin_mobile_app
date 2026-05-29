package com.example.kotlinprogectapp.data.repository

import com.example.kotlinprogectapp.data.remote.api.ApiService
import com.example.kotlinprogectapp.data.remote.dto.*
import com.example.kotlinprogectapp.data.storage.TokenStorage
import com.example.kotlinprogectapp.domain.model.RegisterData
import com.example.kotlinprogectapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = api.login(LoginDto(login, password))
            tokenStorage.saveTokens(response.accessToken, response.refreshToken ?: "")
            val me = api.getMe()
            tokenStorage.saveUserId(me.id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(data: RegisterData): Result<Unit> {
        return try {
            val response = api.register(
                RegisterDto(data.username, data.tag, data.email, data.password)
            )
            tokenStorage.saveTokens(response.accessToken, response.refreshToken ?: "")
            val me = api.getMe()
            tokenStorage.saveUserId(me.id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkTagAvailability(tag: String): Result<Boolean> {
        return try {
            val response = api.checkTag(tag)
            Result.success(response.available)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenStorage.clear()
    }

    override fun isLoggedIn(): Boolean =
        tokenStorage.getAccessToken() != null
}