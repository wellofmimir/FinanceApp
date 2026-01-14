package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale


@Composable
fun ImageSection (
    imageBitmap: ImageBitmap?,
    modifier: Modifier = Modifier,
    onImageClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .border (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp),
                width = 1.dp
            )
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .clickable (
                enabled = imageBitmap != null
            ) {
                onImageClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageBitmap?.let { image ->
            Image (
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}