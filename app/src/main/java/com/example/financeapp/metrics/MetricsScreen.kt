package com.example.financeapp.metrics

import com.example.financeapp.receiptsscreen.ReceiptSectionsViewModel
import com.example.financeapp.TutorialInformation
import com.example.financeapp.receiptsscreen.SinceWhenSection
import com.example.financeapp.receiptsscreen.Timespan
import com.example.financeapp.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.rotate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import java.math.RoundingMode
import kotlin.math.exp

@Composable
fun MetricsScreen (
    modifier: Modifier = Modifier,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    var timespan by remember { mutableStateOf(Timespan.THIS_MONTH) }
    val expenses by receiptSectionsViewModel.expenses.collectAsState()
    val expense by receiptSectionsViewModel.expense.collectAsState()
    val currency by receiptSectionsViewModel.currency.collectAsState()
    var showExpense by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        receiptSectionsViewModel.getCurrency()
    }

    LaunchedEffect(timespan) {
        when (timespan) {
            Timespan.THIS_MONTH -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
            Timespan.LAST_TWO_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfTwoMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
            Timespan.LAST_SIX_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfSixMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
            Timespan.WHOLE_YEAR -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfAYearAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
            Timespan.NONE -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
            Timespan.ALL -> receiptSectionsViewModel.getReceipts()
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SinceWhenSection (
            onCurrentMonth = {
                timespan = it
            },
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        Spacer(
            modifier = Modifier
                .padding(2.dp)
        )

        if (showExpense) {
            AlertDialog (
                modifier = Modifier
                    .background (
                        color = colors.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .height(185.dp),
                onDismissRequest = {
                    showExpense = false
                },
                title = {
                    Text (
                        text = expense.category,
                        color = colors.secondary
                    )
                },
                text = {
                    Text (
                        text = "You've spent: " + if (currency.length == 1) currency + " " + expense.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() else expense.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() + " " + currency,
                        color = colors.secondary
                    )
                },
                confirmButton = {
                    TextButton (
                        modifier = Modifier
                            .border (
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background (
                                color = colors.secondary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        onClick = {
                            showExpense = false
                        }
                    ) {
                        Text (
                            text = "Okay",
                            color = colors.primary
                        )
                    }
                },
                containerColor = colors.primary
            )
        }

        if (expenses.isEmpty()) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .border (
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = colors.secondary
                    )
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = colors.secondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = "No Expenses Yet",
                    fontSize = 24.sp,
                    color = colors.primary
                )
            }
        } else {
            BarChart (
                expenses,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                onSeeExpenseAmount = { expense ->
                    receiptSectionsViewModel.getExpense(expense.category)
                    showExpense = true
                },
                context
            )

            Spacer (
                modifier = Modifier
                    .height(12.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                expenses.forEach {
                    Text (
                        text = it.short,
                        color = colors.secondary,
                        modifier = Modifier
                            .weight(1f)
                            .rotate(-60f)
                            .clickable () {
                                receiptSectionsViewModel.getExpense(it.category)
                                showExpense = true
                            },
                        fontSize = 10.sp,
                        textAlign = TextAlign.Justify,
                        softWrap = false
                    )
                }
            }
        }
    }
}