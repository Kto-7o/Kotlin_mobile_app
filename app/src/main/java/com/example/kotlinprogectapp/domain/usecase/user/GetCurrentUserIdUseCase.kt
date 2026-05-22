package com.example.kotlinprogectapp.domain.usecase.user

import com.example.kotlinprogectapp.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): Long? = repository.getCurrentUserId()
}