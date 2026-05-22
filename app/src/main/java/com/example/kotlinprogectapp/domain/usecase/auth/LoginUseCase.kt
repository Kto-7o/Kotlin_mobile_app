package com.example.kotlinprogectapp.domain.usecase.auth


import com.example.kotlinprogectapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String) =
        repository.login(login.trim(), password)
}