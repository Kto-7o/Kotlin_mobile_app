package com.example.kotlinprogectapp.domain.usecase.user

import com.example.kotlinprogectapp.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Long?) =
        if (userId == null) repository.getMe()
        else repository.getUserById(userId)
}