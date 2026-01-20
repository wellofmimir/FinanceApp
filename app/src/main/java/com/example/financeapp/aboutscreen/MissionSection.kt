package com.example.financeapp.aboutscreen
import com.example.financeapp.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun MissionSection (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Box (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text (
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
                    append("Cheers!\n\n")
                }

                withStyle (
                    style = SpanStyle (
                        color = colors.primary
                    )
                ) {
                    append("The Greeen Team")
                }
            },
            color = colors.primary,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
        )
    }
}