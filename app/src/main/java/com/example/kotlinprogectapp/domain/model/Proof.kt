package com.example.kotlinprogectapp.domain.model

import java.time.LocalDate

data class Proof(
    val id: Long,
    val userId: Long,
    val userName: String,
    val mediaUrl: String,
    val createdAt: LocalDate
)