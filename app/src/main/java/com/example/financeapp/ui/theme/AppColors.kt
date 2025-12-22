package com.example.financeapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf
data class AppColors (
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}

val GreenAppColors = AppColors (
    primary = Pistachio,
    secondary = Emerald,
    background = Emerald,
    surface = Pistachio,
    textPrimary = Emerald,
    textSecondary = Pistachio
)

val CharcoalAppColors = AppColors (
    primary = CharcoalGreen,
    secondary = SoftMoss,
    background = WarmLightStone,
    surface = CharcoalGreen,
    textPrimary = WarmLightStone,
    textSecondary = SlateBlueGray
)