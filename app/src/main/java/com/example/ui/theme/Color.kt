package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Subir Brand Identity Colors
val SubirCyan = Color(0xFF00E5FF)
val SubirBlue = Color(0xFF2979FF)
val SubirPurple = Color(0xFF7C4DFF)
val SubirMagenta = Color(0xFFFF2A85)
val SubirOrange = Color(0xFFFF6D00)
val SubirYellow = Color(0xFFFFD600)

// Dark Palette
val SubirDarkCanvas = Color(0xFF090B10)
val SubirDarkSurface = Color(0xFF131722)
val SubirDarkCard = Color(0xFF1B2030)
val SubirDarkBorder = Color(0xFF272F45)
val SubirDarkTextPrimary = Color(0xFFF8FAFC)
val SubirDarkTextSecondary = Color(0xFF94A3B8)
val SubirDarkTextMuted = Color(0xFF64748B)

// Light Palette
val SubirLightCanvas = Color(0xFFF6F8FC)
val SubirLightSurface = Color(0xFFFFFFFF)
val SubirLightCard = Color(0xFFFFFFFF)
val SubirLightBorder = Color(0xFFE2E8F0)
val SubirLightTextPrimary = Color(0xFF0F172A)
val SubirLightTextSecondary = Color(0xFF475569)
val SubirLightTextMuted = Color(0xFF94A3B8)

// Brand Gradients
val SubirBrandGradient = Brush.linearGradient(
    colors = listOf(SubirCyan, SubirPurple, SubirMagenta)
)

val SubirStoryGradient = Brush.linearGradient(
    colors = listOf(SubirYellow, SubirOrange, SubirMagenta, SubirPurple)
)

val SubirAccentGradient = Brush.horizontalGradient(
    colors = listOf(SubirPurple, SubirMagenta)
)
