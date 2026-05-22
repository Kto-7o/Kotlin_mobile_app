package com.example.kotlinprogectapp.data.repository

import com.example.kotlinprogectapp.data.local.dao.ChallengeDao
import com.example.kotlinprogectapp.data.local.entity.toEntity
import com.example.kotlinprogectapp.data.local.entity.toDomain
import com.example.kotlinprogectapp.data.remote.api.ApiService
import com.example.kotlinprogectapp.data.remote.dto.*
import com.example.kotlinprogectapp.domain.model.*
import com.example.kotlinprogectapp.domain.repository.ChallengeRepository
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: ChallengeDao
) : ChallengeRepository {

    override suspend fun getChallenges(tab: FeedTab): Result<List<Challenge>> {
        return try {
            val dtos = api.getChallenges(tab.name.lowercase())
            val challenges = dtos.map { it.toDomain() }
            // обновляем кэш: сначала чистим старые, потом пишем свежие
            dao.deleteByTab(tab.name.lowercase())
            dao.insertAll(dtos.map { it.toEntity(tab) })
            Result.success(challenges)
        } catch (e: Exception) {
            // нет сети — отдаём кэш
            val cached = dao.getChallengesByTab(tab.name.lowercase())
                .first()
                .map { it.toDomain() }
            if (cached.isNotEmpty()) Result.success(cached)
            else Result.failure(e)
        }
    }

    override suspend fun createChallenge(data: CreateChallengeData): Result<Challenge> {
        return try {
            val dto = api.createChallenge(
                CreateChallengeDto(
                    title          = data.title,
                    description    = data.description,
                    proofType      = data.proofType.name,
                    deadlineDays   = data.deadlineDays,
                    invitedUserIds = data.invitedFriendIds
                )
            )
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProof(challengeId: Long, file: File): Result<Proof> {
        return try {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", file.name, requestBody)
            val dto = api.uploadProof(challengeId, part)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitVerdict(proofId: Long, accepted: Boolean): Result<Unit> {
        return try {
            api.submitVerdict(0L, VerdictDto(proofId, accepted))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}