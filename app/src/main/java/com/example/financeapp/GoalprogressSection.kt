package com.example.financeapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.Pistachio
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.setValue
import kotlin.math.exp

@Composable
fun GoalprogressSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    val goalprogressSectionViewModel: GoalprogressSectionViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)

                return GoalprogressSectionViewModel(repository) as T
            }
        }
    )

    var currentGoal by remember { mutableStateOf("Save $500") }

    Column (
        modifier = modifier
            .aspectRatio(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = Pistachio
                )
                .padding(8.dp)
        ) {
            Box (
                modifier = Modifier
                    .weight(1f)
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = Pistachio
                    )
            ) {
                Text (
                    text = "Current goal:\n$currentGoal",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            var expanded by remember {mutableStateOf(false)}

            Image (
                painter = painterResource(com.example.financeapp.R.drawable.doppelpfeilerunter_foreground),
                contentDescription = "Doppelpfeile",
                modifier = Modifier
                    .height(64.dp)
                    .aspectRatio(1f)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expanded = true
                    }
            )

            SwapCurrentGoalMenu (
                expanded = expanded,
                onCurrentGoalChanged = {newText -> currentGoal = newText},
                onDissmissRequest = { expanded = false},
                onFinished = { expanded = false }
            )
        }

        Row ( //1
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f)
                .background(
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = Pistachio
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "90%",
                fontSize = 64.sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold
            )
        }
    }
}