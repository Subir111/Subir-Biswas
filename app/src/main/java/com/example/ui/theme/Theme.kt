package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.data.repository.SubirRepository
import com.example.data.repository.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = SubirCyan,
    onPrimary = Color.Black,
    primaryContainer = SubirPurple,
    onPrimaryContainer = Color.White,
    secondary = SubirMagenta,
    onSecondary = Color.White,
    secondaryContainer = SubirDarkCard,
    onSecondaryContainer = SubirDarkTextPrimary,
    tertiary = SubirYellow,
    background = SubirDarkCanvas,
    onBackground = SubirDarkTextPrimary,
    surface = SubirDarkSurface,
    onSurface = SubirDarkTextPrimary,
    surfaceVariant = SubirDarkCard,
    onSurfaceVariant = SubirDarkTextSecondary,
    outline = SubirDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SubirPurple,
    onPrimary = Color.White,
    primaryContainer = SubirCyan,
    onPrimaryContainer = Color.Black,
    secondary = SubirMagenta,
    onSecondary = Color.White,
    secondaryContainer = SubirLightCard,
    onSecondaryContainer = SubirLightTextPrimary,
    tertiary = SubirOrange,
    background = SubirLightCanvas,
    onBackground = SubirLightTextPrimary,
    surface = SubirLightSurface,
    onSurface = SubirLightTextPrimary,
    surfaceVariant = SubirLightCard,
    onSurfaceVariant = SubirLightTextSecondary,
    outline = SubirLightBorder
)

@Composable
fun SubirTheme(
    content: @Composable () -> Unit
) {
    val themeMode by SubirRepository.themeMode.collectAsState()
    val isSystemDark = isSystemInDarkTheme()

    val useDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemDark
    }

    val colorScheme = if (useDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
