package studio.lemniscate.greeen.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

import android.content.res.Configuration

data class AppTypography (
    val title: TextUnit,
    val subtitle: TextUnit,
    val body: TextUnit,
    val button: TextUnit,
    val medium: TextUnit,
    val small: TextUnit,
)

val SmallTypography = AppTypography (
    title = 20.sp,
    subtitle = 18.sp,
    body = 16.sp,
    button = 12.sp,
    medium = 14.sp,
    small = 10.sp
)

val NormalTypography = AppTypography (
    title = 22.sp,
    subtitle = 20.sp,
    body = 18.sp,
    button = 14.sp,
    medium = 16.sp,
    small = 12.sp
)

val LargeTypography = AppTypography (
    title = 24.sp,
    subtitle = 22.sp,
    body = 20.sp,
    button = 16.sp,
    medium = 18.sp,
    small = 14.sp
)

val ExtraLargeTypography = AppTypography (
    title = 28.sp,
    subtitle = 24.sp,
    body = 22.sp,
    button = 18.sp,
    medium = 20.sp,
    small = 16.sp
)

val ExtraExtraLargeTypography = AppTypography (
    title = 30.sp,
    subtitle = 26.sp,
    body = 22.sp,
    button = 18.sp,
    medium = 20.sp,
    small = 16.sp
)
val LocalAppTypography = staticCompositionLocalOf {
    NormalTypography
}

fun getTypography (
    configuration: Configuration
): AppTypography {
    val screenHeight = configuration.screenHeightDp

    return when {
        screenHeight <= 640 -> SmallTypography
        screenHeight <= 760 -> NormalTypography
        screenHeight <= 840 -> LargeTypography
        screenHeight <= 920 -> ExtraLargeTypography
        else -> ExtraExtraLargeTypography
    }
}