package studio.lemniscate.greeen.aboutscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.sizeIn

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun AppLogoSection (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Box (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image (
            painter = painterResource(R.mipmap.applogo_foreground),
            contentDescription = "AppLogo-Mission",
            modifier = Modifier
                .sizeIn(125.dp, 125.dp, 192.dp, 192.dp)
                .background (
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
}