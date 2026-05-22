package com.example.kotlinprogectapp.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinprogectapp.data.local.dao.ChallengeDao
import com.example.kotlinprogectapp.data.local.dao.UserDao
import com.example.kotlinprogectapp.data.local.entity.ChallengeEntity
import com.example.kotlinprogectapp.data.local.entity.UserEntity

@Database(
    entities = [ChallengeEntity::class, UserEntity::class],
    version  = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun userDao(): UserDao
}