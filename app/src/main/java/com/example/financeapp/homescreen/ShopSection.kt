package com.example.financeapp.homescreen
import com.example.financeapp.R
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.text.Layout
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun ShopSection(modifier: Modifier = Modifier, context: Context = LocalContext.current, shopSectionClicked: () -> Unit) {

    val colors = LocalAppColors.current

    Box (
        modifier = modifier
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                shopSectionClicked()
            },
        contentAlignment = Alignment.TopEnd
    ) {
        Box (
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background (
                    color = colors.background,
                    shape = CircleShape
                )
                .border (
                    width = 1.dp,
                    color = colors.background,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image (
                painter = painterResource(R.drawable.dollarsign_foreground),
                contentDescription = "Dollar",
                modifier = Modifier
                    .size(16.dp)
                    .background (
                        color = colors.background,
                        shape = CircleShape
                    )
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                    },
                colorFilter = ColorFilter.tint(colors.surface),
                contentScale = ContentScale.Fit,
                alignment = Alignment.BottomEnd
            )
        }

        Text (
            text = "Theme\nShop",
            color = colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp)
        )
    }
}