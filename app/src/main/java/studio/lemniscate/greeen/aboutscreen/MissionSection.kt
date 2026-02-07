package studio.lemniscate.greeen.aboutscreen

import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.material3.Text

@Composable
fun MissionSection (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val maxFontSize: TextUnit = 24.sp
    val minFontSize: TextUnit = 18.sp

    var readyToDraw by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(maxFontSize) }

    Box (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text (
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                .drawWithContent {
                    if (readyToDraw)
                        drawContent()
                },
            text = buildAnnotatedString {
                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("We started Greeen as a way to help people ")
                }

                append("learn more about themselves. Whether it’s through empowering a sense of worth or simply helping reflect on spending habits, it is our mission to make life easier, one receipt at a time!\n\n")
                append("As a team of two, we develop human made products that are in touch with the current goings-on in the world. We pride ourselves heavily on our approach, and are committed to being a people-first service in our process and our output.\n\n")
                append("We hope our app helps you navigate the insanely confusing world of finance a little easier. If you have any suggestions for us")


                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append(", please check out our feedback form ")
                }

                withStyle (
                    style = SpanStyle (
                        color = colors.primary
                    )
                ) {
                    append("in user settings.\n\n")
                }

                // Cheers – bold
                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Cheers!\n")
                }

                withStyle (
                    style = SpanStyle (
                        color = colors.primary
                    )
                ) {
                    append("The Greeen Team")
                }
            },
            fontSize = fontSize,
            color = colors.primary,
            textAlign = TextAlign.Left,
            onTextLayout = { result ->
                if (result.didOverflowHeight  && fontSize > minFontSize) {
                    fontSize *= 0.8f
                } else {
                    readyToDraw = true
                }
            }
        )
    }
}