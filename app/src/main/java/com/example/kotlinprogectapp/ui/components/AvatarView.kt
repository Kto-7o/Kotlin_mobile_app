package com.example.kotlinprogectapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Генерируем цвет фона из хэша имени чтобы у каждого пользователя
// был свой цвет аватара, не случайный при каждой перерисовке
fun avatarColor(name: String): Color {
    val colors = listOf(
        Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFF10B981),
        Color(0xFFF59E0B), Color(0xFFEF4444), Color(0xFFEC4899)
    )
    return colors[name.hashCode().absoluteValue % colors.size]
}

private val Int.absoluteValue get() = if (this < 0) -this else this

@Composable
fun AvatarView(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    val initials = name
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }

    Box(
        modifier        = modifier
            .size(size)
            .clip(CircleShape)
            .background(avatarColor(name)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = initials,
            color      = Color.White,
            fontSize   = (size.value * 0.35f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
