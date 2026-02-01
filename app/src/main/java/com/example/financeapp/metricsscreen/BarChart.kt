package com.example.financeapp.metricsscreen

import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.database.Expense

import android.content.Context
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

import kotlin.collections.forEachIndexed

@Composable
fun BarChart (
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    onSeeExpenseAmount: (Expense) -> Unit,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current

    Canvas (
        modifier = modifier
            .border(1.dp, colors.secondary, RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .pointerInput(expenses) {
                detectTapGestures { offset ->
                    var maxAmount = expenses.maxOf { it.amount }

                    if (maxAmount == 0f)
                        maxAmount = 1f

                    val barHeight = size.height / (expenses.size * 1f)

                    expenses.forEachIndexed { index, expense ->
                        val barWidth = (expense.amount / maxAmount) * (size.width * 1.5f)
                        val y = index * barHeight * 1f

                        if (offset.x in 0f..barWidth && offset.y in y..(y + barHeight)) {
                            onSeeExpenseAmount(expense)
                        }
                    }
                }
            }
    ) {
        var maxAmount = expenses.maxOf { it.amount }

        if (maxAmount == 0f)
            maxAmount = 1f

        val barHeight = size.height / (expenses.size * 1f)

        expenses.forEachIndexed { index, expense ->
            var barWidth = (expense.amount / maxAmount) * (size.width * 0.9f)

            if (barWidth > 0f && barWidth < 15f)
                barWidth = 15f

            val y = index * barHeight * 1f

            drawRect (
                color = colors.secondary,
                topLeft = Offset(0f, y),
                size = Size(barWidth, barHeight)
            )

            drawRect (
                color = colors.primary,
                topLeft = Offset(1f, y + 1),
                size = Size (
                    barWidth - 1 * 2,
                    barHeight - 1 * 2
                )
            )
        }
    }

}