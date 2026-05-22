package com.example.kotlinprogectapp.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinprogectapp.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val username: String,
    val tag: String,
    val email: String
)

fun UserEntity.toDomain(): User = User(
    id       = id,
    username = username,
    tag      = tag,
    email    = email
)