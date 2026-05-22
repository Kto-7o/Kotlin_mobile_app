package com.example.kotlinprogectapp.domain.usecase.auth

import com.example.kotlinprogectapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckTagUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(tag: String) = repository.checkTagAvailability(tag)
}