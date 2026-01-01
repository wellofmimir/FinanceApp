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
    GreenAppColors
}
val GreenAppColors = AppColors (
    primary = Emerald,
    secondary = Pistachio,
    background = Emerald,
    surface = Pistachio,
    textPrimary = Emerald,
    textSecondary = Pistachio
)

val ElectricAppColors = AppColors (
    primary = CalmBlue,
    secondary = ElectricPurple,
    background = CalmBlue,
    surface = ElectricPurple,
    textPrimary = CalmBlue,
    textSecondary = ElectricPurple
)

val AzureAppColors = AppColors (
    primary = AzureBlue,
    secondary = AzureComplimentary,
    background = AzureBlue,
    surface = AzureComplimentary,
    textPrimary = AzureBlue,
    textSecondary = AzureBlue
)

val PeachAppColors = AppColors (
    primary = Peach,
    secondary = PeachComplimentary,
    background = Peach,
    surface = PeachComplimentary,
    textPrimary = Peach,
    textSecondary = Peach
)

val CharcoalAppColors = AppColors (
    primary = CharcoalGreen,
    secondary = WarmLightStone,
    background = CharcoalGreen,
    surface = WarmLightStone,
    textPrimary = CharcoalGreen,
    textSecondary = WarmLightStone
)