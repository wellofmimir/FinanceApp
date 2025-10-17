package com.example.financeapp

import kotlinx.coroutines.*
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.height
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@Composable
fun AddNewGoalMenu(expanded: Boolean, onDismissRequest: () -> Unit, onFinished: () -> Unit, context: Context = LocalContext.current) {


    val addNewGoalMenuViewModel: AddNewGoalMenuViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {
                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)
                return AddNewGoalMenuViewModel(repository) as T
            }
        }
    )

    var errorInInput by remember { mutableStateOf(false) }
    var nameOfGoalText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0.0f) }
    var feedbackTrigger by remember { mutableStateOf(0) } // für den LaunchedEffect weiter unten
    var amountText by remember { mutableStateOf("") }

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .clip (
                RoundedCornerShape(12.dp)
            )
            .shadow (
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp))
            .background (
                color = Emerald
            )
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        containerColor = Color.Transparent

    ) {

        Column (
            modifier = Modifier
                .background (
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text = "New Goal",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer (
                modifier = Modifier
                    .height(8.dp)
            )

            HorizontalDivider (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.White
            )

            Spacer (
                modifier = Modifier
                    .height(48.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text (
                    text = "Name",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                )

                TextField (
                    value = nameOfGoalText,
                    onValueChange = { newText ->
                        nameOfGoalText = newText
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors (
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            Spacer (
                modifier = Modifier
                    .height(24.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text (
                    text = "Amount",
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                )


                TextField (
                    value = amountText,
                    onValueChange = { newText ->

                        if (amountText.isEmpty())
                            amount = 0.0f

                        val moneyRegex = Regex("^\\d+(\\.\\d{0,2})?\$")

                        if (moneyRegex.matches(newText)) {
                            amountText = newText
                            amount = newText.toFloatOrNull() ?: 0f
                        }

                        if (newText.isEmpty())
                            amountText = ""
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors (
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions (
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            Spacer (
                modifier = Modifier
                    .height(80.dp)
            )

            var confirmed by remember { mutableStateOf(false) }

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button (
                    onClick = {

                        if (!nameOfGoalText.isEmpty() && amount > 0.0f) {

                            confirmed = true
                            addNewGoalMenuViewModel.newGoalAdded(nameOfGoalText, amount, "InProgress")

                            amount = 0.0f
                            nameOfGoalText = ""
                        } else {
                            errorInInput = true
                        }

                        feedbackTrigger++
                    },
                    colors = ButtonDefaults.buttonColors (
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = BorderStroke (
                        width = 1.dp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                ) {
                    Text (
                        text = when {
                            errorInInput -> "x"
                            confirmed -> "✓"
                            else -> "Add"
                        }
                    )
                }

                LaunchedEffect (feedbackTrigger) {
                    delay (1000)
                    errorInInput = false

                    if (confirmed) {
                        amountText = ""
                        nameOfGoalText = ""
                        onFinished()
                    }
                }
            }
        }
    }
}