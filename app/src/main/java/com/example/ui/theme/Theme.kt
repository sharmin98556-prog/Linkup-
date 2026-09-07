package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FrendoBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = FrendoAccentGreen,
    onSecondary = Color.White,
    background = FrendoBgDark,
    onBackground = FrendoTextPrimaryDark,
    surface = FrendoSurfaceDark,
    onSurface = FrendoTextPrimaryDark,
    surfaceVariant = FrendoBtnSecondaryDark,
    onSurfaceVariant = FrendoTextSecondaryDark,
    outline = FrendoCardBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = FrendoBlue,
    onPrimary = Color.White,
    primaryContainer = FrendoBlueLight,
    onPrimaryContainer = FrendoBlueDark,
    secondary = FrendoAccentGreen,
    onSecondary = Color.White,
    background = FrendoBgLight,
    onBackground = FrendoTextPrimaryLight,
    surface = FrendoSurfaceLight,
    onSurface = FrendoTextPrimaryLight,
    surfaceVariant = FrendoBtnSecondaryLight,
    onSurfaceVariant = FrendoTextSecondaryLight,
    outline = FrendoCardBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

