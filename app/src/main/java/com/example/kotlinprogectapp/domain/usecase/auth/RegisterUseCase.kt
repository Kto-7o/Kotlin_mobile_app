package com.example.kotlinprogectapp.domain.usecase.auth

import com.example.kotlinprogectapp.domain.model.RegisterData
import com.example.kotlinprogectapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(data: RegisterData) = repository.register(data)
}