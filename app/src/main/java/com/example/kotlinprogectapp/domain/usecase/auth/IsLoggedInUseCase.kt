package com.example.kotlinprogectapp.domain.usecase.auth

import com.example.kotlinprogectapp.domain.repository.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.isLoggedIn()
}