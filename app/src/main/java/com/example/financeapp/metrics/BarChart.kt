package com.example.financeapp.metrics

import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.database.Expense

import android.content.Context
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box

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
            .border (
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = colors.secondary
            )
            .background (
                shape = RoundedCornerShape(12.dp),
                color = colors.secondary
            )
            .pointerInput(expenses) {
                detectTapGestures { offset ->
                    val barWidth = size.width / (expenses.size * 2f)

                    var maxAmount = expenses.maxOf { it.amount }

                    if (maxAmount == 0f)
                        maxAmount = 1f

                    expenses.forEachIndexed { index, expense ->
                        val x = index * barWidth * 2 + barWidth / 2
                        val barHeight = (expense.amount / maxAmount) * (size.height * 0.9f)
                        val y = size.height - barHeight

                        if (offset.x in x..(x + barWidth) && offset.y in y..size.height.toFloat())
                            onSeeExpenseAmount(expense)
                    }
                }
            }
    ) {
        var maxAmount = expenses.maxOf { it.amount }

        if (maxAmount == 0f)
            maxAmount = 1f

        val barWidth = size.width / (expenses.size * 2f)

        expenses.forEachIndexed { index, expense ->
            val barHeight = (expense.amount / maxAmount) * (size.height * 0.9f)

            val x = index * barWidth * 2 + barWidth / 2
            val y = (size.height) - barHeight

            drawRect (
                color = colors.primary,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }
}