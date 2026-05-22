package com.example.kotlinprogectapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge  = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 24.sp, color = TextPrimary),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 20.sp, color = TextPrimary),
    titleLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = TextPrimary),
    titleMedium    = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = TextPrimary),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, color = TextSecond),
    labelSmall     = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, color = TextSecond)

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)