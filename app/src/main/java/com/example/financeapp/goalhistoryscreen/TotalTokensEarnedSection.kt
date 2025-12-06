package com.example.financeapp.goalhistoryscreen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.theme.Pistachio

@Composable
fun TotalTokensEarnedSection(modifier: Modifier = Modifier) {

    Box (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text (
            text = "BUMSEREI"
        )
    }

}