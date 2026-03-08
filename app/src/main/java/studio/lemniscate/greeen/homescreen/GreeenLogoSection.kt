package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import studio.lemniscate.greeen.ui.theme.Emerald
import studio.lemniscate.greeen.ui.theme.Pistachio

@Composable
fun GreeenLogoSection (
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .border (
                width = 1.dp,
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer (
            modifier = Modifier
                .height(8.dp)
        )

        Image (
            painter = painterResource(R.drawable.logo_greeen),
            contentDescription = "AppLogoHomeScreen",
            modifier = Modifier
                .weight(1f)
                .sizeIn(32.dp, 32.dp, 48.dp, 48.dp)
                .background (
                    color = Emerald,
                    shape = CircleShape
                )
                .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                },
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        )

        Spacer (
            modifier = Modifier
                .height(2.dp)
        )

        Text (
            text = buildAnnotatedString {
                withStyle (
                    SpanStyle (
                        fontSize = 18.sp
                    )
                ) {
                    append("Greeen")
                }

                append("\n")

                withStyle(
                    SpanStyle (
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        color = Pistachio.copy(alpha = 0.7f)
                    )
                ) {
                    append("Studio Lemniscate")
                }
            },
            color = Pistachio,
            textAlign = TextAlign.Center
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )
    }
}