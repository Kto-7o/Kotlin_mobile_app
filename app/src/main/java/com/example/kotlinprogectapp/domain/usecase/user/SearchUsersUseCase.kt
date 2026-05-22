package com.example.kotlinprogectapp.domain.usecase.user

import com.example.kotlinprogectapp.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(query: String) = repository.searchUsers(query)
}