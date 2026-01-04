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
    secondary = Lilac,
    background = CalmBlue,
    surface = Lilac,
    textPrimary = CalmBlue,
    textSecondary = Lilac
)

val AzureAppColors = AppColors (
    primary = AzureBlue,
    secondary = ComplementaryOrange,
    background = AzureBlue,
    surface = ComplementaryOrange,
    textPrimary = AzureBlue,
    textSecondary = AzureBlue
)

val PeachAppColors = AppColors (
    primary = Peach,
    secondary = Sunny,
    background = Peach,
    surface = Sunny,
    textPrimary = Peach,
    textSecondary = Sunny
)

val CharcoalAppColors = AppColors (
    primary = CharcoalGreen,
    secondary = Steel,
    background = CharcoalGreen,
    surface = Steel,
    textPrimary = CharcoalGreen,
    textSecondary = Steel
)