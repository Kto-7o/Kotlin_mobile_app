package com.example.kotlinprogectapp.di

import android.content.Context
import androidx.room.Room
import com.example.kotlinprogectapp.data.local.AppDatabase
import com.example.kotlinprogectapp.data.local.dao.ChallengeDao
import com.example.kotlinprogectapp.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "challenge_db")
            .fallbackToDestructiveMigration()  // при смене схемы пересоздаёт БД
            .build()

    @Provides
    fun provideChallengeDao(db: AppDatabase): ChallengeDao = db.challengeDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}