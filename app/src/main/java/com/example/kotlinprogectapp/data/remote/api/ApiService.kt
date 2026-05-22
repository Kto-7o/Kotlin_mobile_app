package com.example.kotlinprogectapp.data.remote.api

import com.example.kotlinprogectapp.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body body: LoginDto): TokenDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterDto): TokenDto

    @GET("auth/check-tag")
    suspend fun checkTag(@Query("tag") tag: String): TagCheckDto

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: RefreshDto): TokenDto

    // Challenges

    @GET("challenges")
    suspend fun getChallenges(@Query("tab") tab: String): List<ChallengeDto>

    @POST("challenges")
    suspend fun createChallenge(@Body body: CreateChallengeDto): ChallengeDto

    @Multipart
    @POST("challenges/{id}/proof")
    suspend fun uploadProof(
        @Path("id") challengeId: Long,
        @Part photo: MultipartBody.Part
    ): ProofDto

    @POST("challenges/{id}/verdict")
    suspend fun submitVerdict(
        @Path("id") challengeId: Long,
        @Body body: VerdictDto
    ): Response<Unit>

    // Friends

    @GET("friends")
    suspend fun getFriends(): List<UserDto>

    @GET("friends/requests")
    suspend fun getFriendRequests(): FriendRequestsDto

    @POST("friends/request/{userId}")
    suspend fun sendFriendRequest(@Path("userId") userId: Long): Response<Unit>

    @POST("friends/request/{id}/respond")
    suspend fun respondToRequest(
        @Path("id") requestId: Long,
        @Body body: RespondDto
    ): Response<Unit>

    @DELETE("friends/{userId}")
    suspend fun deleteFriend(@Path("userId") userId: Long): Response<Unit>

    // Users

    @GET("users/me")
    suspend fun getMe(): UserDto

    @GET("users/{id}")
    suspend fun getProfile(@Path("id") userId: Long): UserDto

    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): List<UserDto>
}