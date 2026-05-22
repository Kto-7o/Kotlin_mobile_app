package com.example.kotlinprogectapp.domain.repository

import com.example.kotlinprogectapp.domain.model.*
import java.io.File

interface ChallengeRepository {
    suspend fun getChallenges(tab: FeedTab): Result<List<Challenge>>
    suspend fun createChallenge(data: CreateChallengeData): Result<Challenge>
    suspend fun uploadProof(challengeId: Long, file: File): Result<Proof>
    suspend fun submitVerdict(proofId: Long, accepted: Boolean): Result<Unit>
}