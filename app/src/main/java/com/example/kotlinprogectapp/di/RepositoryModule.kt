package com.example.kotlinprogectapp.di

import com.example.kotlinprogectapp.data.repository.*
import com.example.kotlinprogectapp.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindChallengeRepo(impl: ChallengeRepositoryImpl): ChallengeRepository

    @Binds @Singleton
    abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindFriendRepo(impl: FriendRepositoryImpl): FriendRepository

    @Binds @Singleton
    abstract fun bindUserRepo(impl: UserRepositoryImpl): UserRepository
}
