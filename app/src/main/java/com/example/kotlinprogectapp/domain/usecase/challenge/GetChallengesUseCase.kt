package com.example.kotlinprogectapp.domain.usecase.challenge

import com.example.kotlinprogectapp.domain.model.FeedTab
import com.example.kotlinprogectapp.domain.repository.ChallengeRepository
import javax.inject.Inject

class GetChallengesUseCase @Inject constructor(
    private val repository: ChallengeRepository
) {
    suspend operator fun invoke(tab: FeedTab) = repository.getChallenges(tab)
}