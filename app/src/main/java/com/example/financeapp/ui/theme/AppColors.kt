package com.example.financeapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState


@Composable
fun rememberDynamicAppColors(initialColors: AppColors): MutableState<AppColors> {
    return remember { mutableStateOf(initialColors) }
}

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
    primary = ElectricPurple,
    secondary = SoftAmber,
    background = ElectricPurple,
    surface = SoftAmber,
    textPrimary = ElectricPurple,
    textSecondary = SoftAmber
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
    secondary = CalmBlue,
    background = Peach,
    surface = CalmBlue,
    textPrimary = Color.Black,
    textSecondary = CalmBlue
)

val CharcoalAppColors = AppColors (
    primary = CharcoalGreen,
    secondary = WarmLightStone,
    background = WarmLightStone,
    surface = CharcoalGreen,
    textPrimary = CharcoalGreen,
    textSecondary = WarmLightStone
)