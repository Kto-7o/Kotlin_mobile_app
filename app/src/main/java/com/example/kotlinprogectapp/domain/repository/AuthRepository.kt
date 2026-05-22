package com.example.kotlinprogectapp.domain.repository


import com.example.kotlinprogectapp.domain.model.RegisterData

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun register(data: RegisterData): Result<Unit>
    suspend fun checkTagAvailability(tag: String): Result<Boolean>
    suspend fun logout()
    fun isLoggedIn(): Boolean
}