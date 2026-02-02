package com.example.financeapp.welcomescreen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntSize
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.R
import com.example.financeapp.repositories.UserRepository

@Composable
fun FirstGoalMenu(expanded: Boolean, onDismissRequested: () -> Unit, onFinished: (username: String, goal: String, amount: Float, currencySymbol: String) -> Unit) {

    var username by remember { mutableStateOf("") }
    var goal by remember {mutableStateOf("")}
    var amountText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0.0f) }
    var chosenCurrency by remember {mutableStateOf("$")}
    var differentCurrency by remember {mutableStateOf("")}

    val displayMetrics = Resources.getSystem().displayMetrics
    val heightPx = displayMetrics.heightPixels

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = {
            username = ""
            goal = ""
            amountText = ""
            amount = 0.0f

            onDismissRequested()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height((heightPx / 2).dp)
            .background (
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            ),
        containerColor = Color.Transparent
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .background(
                    color = Emerald,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                TextField (
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    placeholder = {
                        Text (
                            text = "Your name...",
                            color = Emerald,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        color = Emerald,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = TextFieldDefaults.colors (
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        cursorColor = Color.Black   ,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                        }
                )
            }

            Spacer (
                modifier = Modifier
                    .background(
                        color = Emerald
                    )
                    .padding(4.dp)
            )

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                Spacer (
                    modifier = Modifier
                        .padding(10.dp)
                )

                Text (
                    text = "Let's Create Your First Goal!",
                    color = Emerald,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer (
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                HorizontalDivider (
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                TextField (
                    value = goal,
                    onValueChange = {
                        goal = it
                    },
                    placeholder = {
                        Text (
                            text = "Goal Name...",
                            color = Emerald,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Start,
                        color = Emerald,
                    ),
                    colors = TextFieldDefaults.colors (
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        cursorColor = Color.Black   ,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                        }
                        .padding(start = 10.dp, end = 10.dp)
                )
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                HorizontalDivider (
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                TextField (
                    value = amountText,
                    onValueChange = {
                        amountText = it
                    },
                    placeholder = {
                        Text (
                            text = "How much cash are you saving?",
                            color = Emerald,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Start,
                        color = Emerald,
                    ),
                    colors = TextFieldDefaults.colors (
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        cursorColor = Color.Black   ,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions (
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                        }
                        .padding(start = 10.dp, end = 10.dp)
                )
            }

            Row (
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .background(
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val currencies = listOf("zł", "€", "$", "¥", "£")

                currencies.forEach {
                    Box (
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                chosenCurrency = it
                                differentCurrency = ""
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text (
                            text = it,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = if (it == chosenCurrency) Color.White else Emerald
                        )
                    }
                }
            }

            Row (
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
                    .padding(start = 5.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                var expandAlertDialog by remember { mutableStateOf(false) }
                var insertedCurrency by remember { mutableStateOf("")}

                if (differentCurrency.isEmpty()) {

                    Text(
                        text = "choose another currency",
                        fontSize = 14.sp,
                        color = Emerald,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                expandAlertDialog = true
                            }
                    )

                    if (expandAlertDialog) {
                        AlertDialog (
                            containerColor = Emerald,
                            modifier = Modifier
                                .background (
                                    color = Emerald,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            onDismissRequest = { },
                            title = {
                                Text (
                                    text = "Tell us your currency",
                                    color = Pistachio,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            text = {
                                OutlinedTextField (
                                    value = insertedCurrency,
                                    onValueChange = { newValue ->
                                        val filtered = newValue.filterNot {
                                            it.isDigit()
                                        }

                                        if (newValue.length <= 3) {
                                            insertedCurrency = filtered.uppercase()
                                        }
                                    },
                                    label = {
                                        Text(
                                            color = Pistachio,
                                            text = "Currency symbol or shortcut",
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background (
                                            color = Emerald,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    colors = OutlinedTextFieldDefaults.colors (
                                        focusedTextColor = Pistachio,
                                        unfocusedTextColor = Pistachio
                                    )
                                )
                            },
                            confirmButton = {
                                TextButton (
                                    onClick = {
                                        if (!insertedCurrency.isEmpty()) {
                                            differentCurrency = insertedCurrency
                                            chosenCurrency = differentCurrency
                                            expandAlertDialog = false
                                        }
                                    }
                                ) {
                                    Text (
                                        text = "Okay",
                                        color = Pistachio
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        expandAlertDialog = false
                                    }
                                ) {
                                    Text (
                                        text = "Cancel",
                                        color = Pistachio
                                    )
                                }
                            }
                        )
                    }
                } else {
                    Box (
                        modifier = Modifier
                            .padding(horizontal = 15.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text (
                            text = chosenCurrency,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        if (expanded) {
            Box (
                modifier = Modifier
                    .background (
                        color = Emerald,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row (
                    ) {
                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_0"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring0_1"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_2"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring0_3"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_4"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_5"
                        )
                    }

                    Row (
                    ) {
                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring1_0"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring1_1"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_2"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_3"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_4"
                        )

                        Image (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    val moneyRegex = Regex("^\\d+(\\.\\d{0,2})?\$")

                                    if (moneyRegex.matches(amountText)) {
                                        amount = amountText.toFloatOrNull() ?: 0f
                                        onFinished(username, goal, amount, chosenCurrency)
                                    } else {
                                        username = ""
                                        amount = 0.0f
                                        goal = ""

                                        onDismissRequested()
                                        //TODO("FEHLERMELDUNG")
                                    }
                                },
                            painter = painterResource(R.drawable.pfeilnachrechtspistachio_foreground),
                            contentDescription = "Ring1_5"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FirstTokenMenu(expanded: Boolean, onDismissRequested: () -> Unit, onFinished: (tokenCount: Int) -> Unit) {

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = onDismissRequested,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .heightIn(450.dp)
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        containerColor = Color.Transparent
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background (
                        color = Pistachio,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                Spacer (
                    modifier = Modifier
                        .padding(5.dp)
                )

                Text (
                    text = "Greeen makes goal setting worth-while with tokens.",
                    color = Emerald,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer (
                    modifier = Modifier
                        .padding(5.dp)
                )

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    HorizontalDivider (
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        thickness = 1.dp,
                        color = Color.Black
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Text (
                        text = "Every goal you create is worth tokens. You decide how much your goals are worth - up to five tokens.",
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Spacer (
                        modifier = Modifier
                            .padding(5.dp)
                    )

                    HorizontalDivider (
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        thickness = 1.dp,
                        color = Color.Black
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Text (
                        text = "How much is this goal worth?",
                        color = Emerald,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Justify
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Spacer (
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                var tokenIdentifier by remember { mutableStateOf(0) }
                var tokenText by remember { mutableStateOf("") }

                tokenText = when (tokenIdentifier) {

                    0 -> "One token: Baby steps get you there."
                    1 -> "Two tokens: Just a little goal."
                    2 -> "Three tokens: Something to strive for."
                    3 -> "Four tokens: A true achievement."
                    4 -> "Five tokens: Extraordinary!"
                    else -> ""
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    ) {
                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = Emerald,
                                style = Fill
                            )
                        }

                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = Emerald,
                                style = if (tokenIdentifier >= 1) Fill else Stroke(4f)
                            )
                        }

                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = Emerald,
                                style = if (tokenIdentifier >= 2) Fill else Stroke(4f)
                            )
                        }

                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = Emerald,
                                style = if (tokenIdentifier >= 3) Fill else Stroke(4f)
                            )
                        }

                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = Emerald,
                                style = if (tokenIdentifier >= 4) Fill else Stroke(4f)
                            )
                        }

                        Spacer (
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }

                Spacer (
                    modifier = Modifier
                        .padding(5.dp)
                )

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .fillMaxHeight(0.3f)
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    ) {
                        Image (
                            painter = painterResource(R.drawable.minuszeichenstandard_foreground),
                            contentDescription = "Minuszeichen_Token",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (tokenIdentifier > 0 && tokenIdentifier <= 4)
                                        --tokenIdentifier
                                    }
                        )

                        Spacer (
                            modifier = Modifier
                                .padding(15.dp)
                        )

                        Image (
                            painter = painterResource(R.drawable.pluszeichenstandard_foreground),
                            contentDescription = "Pluszeichen_Token",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (tokenIdentifier >= 0 && tokenIdentifier < 4)
                                        ++tokenIdentifier
                                }
                        )
                    }
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background (
                            color = Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(start = 5.dp, end = 5.dp)
                ) {
                    Text (
                        text = tokenText,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

                Spacer (
                    modifier = Modifier
                        .padding(50.dp)
                )

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .background (
                            color = Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(start = 5.dp, end = 5.dp)
                        .height(85.dp)
                ) {
                    Text (
                        text = "Start your journey.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(5.dp)
                    )

                    Image (
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onFinished(tokenIdentifier)
                            },
                        painter = painterResource(R.drawable.pfeilnachrechts_foreground),
                        contentDescription = "Ring1_5",
                        colorFilter = ColorFilter.tint(Emerald)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onFinished: () -> Unit, splashMode: Boolean, context: Context = LocalContext.current) {

    val welcomeScreenViewModel: WelcomeScreenViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val userRepository = UserRepository.getInstance(database)
                val goalRepository = GoalRepository.getInstance(database)

                return WelcomeScreenViewModel(userRepository, goalRepository) as T
            }
        }
    )

    var username by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0.0f) }

    val density = LocalDensity.current

    var imagePosition by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    var firstGoalMenuExpanded by remember { mutableStateOf(false) }
    var firstTokenMenuExpanded by remember { mutableStateOf(false) }

    var chosenCurrency by remember { mutableStateOf("$")}

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Emerald
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.Start
        ) {
            FirstTokenMenu (
                expanded = firstTokenMenuExpanded,
                onDismissRequested = {
                    firstTokenMenuExpanded = false
                },
                onFinished = { tokenCount ->

                    firstTokenMenuExpanded = false

                    if (username.isEmpty() || username == "DUMMY") {
                        //TODO: Fehlermeldung anzeigen
                    } else {
                        welcomeScreenViewModel.updateUser(username)
                        welcomeScreenViewModel.insertGoal (
                            Goal (
                                -1,
                                goal,
                                amount,
                                0.0f,
                                1,
                                "",
                                tokenCount + 1,
                                ""
                            )
                        )
                        welcomeScreenViewModel.setCurrency(currency = chosenCurrency)
                        onFinished()
                    }
                }
            )

            Row (
            ) {
                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring0_0"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring0_1"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring0_2"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring0_3"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring0_4"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring0_5"
                )
            }

            Row (
            ) {
                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring1_0"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring1_1"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f) //unsichtbar
                        .onGloballyPositioned { layoutCoordinates ->
                            imagePosition = layoutCoordinates.positionInWindow()
                            imageSize = layoutCoordinates.size * 2
                        },
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring1_2"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f), //unsichtbar
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring1_3"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring1_4"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring1_5"
                )
            }

            Row (
            ) {
                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring2_0"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring2_1"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring2_2"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring2_3"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring2_4"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring2_5"
                )
            }

            Row (
            ) {
                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring3_0"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring3_1"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring3_2"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring3_3"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring3_4"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring3_5"
                )
            }

            Row (
            ) {
                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring4_0"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring4_1"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring4_2"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring4_3"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .alpha(0f),
                    painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                    contentDescription = "Ring4_4"
                )

                Image (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                    contentDescription = "Ring4_5"
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FirstGoalMenu (
                    expanded = firstGoalMenuExpanded,
                    onDismissRequested = {
                        firstGoalMenuExpanded = false
                    },
                    onFinished = { newUsername, firstGoal, savingAmount, currency  ->

                        username = newUsername
                        goal = firstGoal
                        amount = savingAmount
                        firstGoalMenuExpanded = false
                        firstTokenMenuExpanded = true
                        chosenCurrency = currency

                        if (goal.isEmpty() || amount == 0.0f)
                            TODO("Fehlermeldung einbauen")
                    }
                )
            }

            Column (
                verticalArrangement = Arrangement.Center
            ) {
                Spacer (
                    modifier = Modifier
                        .padding(35.dp)
                )

                Text (
                    modifier = Modifier
                        .padding(start = 25.dp),
                    text = if (splashMode) "Greeen." else "Welcome to Greeen.",
                    color = Pistachio,
                    fontSize = if (splashMode) 48.sp else 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!splashMode) {

                Column (
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer (
                        modifier = Modifier
                            .padding(10.dp)
                    )

                    Text (
                        modifier = Modifier
                            .padding(start = 25.dp, end = 25.dp),
                        text = "To get started, please tell us a few details about you...",
                        color = Pistachio,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Column (
                    modifier = Modifier
                        .offset(x = (LocalConfiguration.current.screenWidthDp * 0.34).dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer (
                        modifier = Modifier
                            .padding(20.dp)
                    )

                    Box (
                        modifier = Modifier
                            .background(
                                color = Pistachio,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .height(65.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                firstGoalMenuExpanded = true
                            }
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                        ) {

                            Text (
                                modifier = Modifier
                                    .padding(start = 25.dp, end = 25.dp),
                                text = "Let's go!",
                                color = Emerald,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Image (
                                painter = painterResource(R.drawable.pfeilnachrechts_foreground),
                                contentDescription = "PfeilNachRechts",
                                colorFilter = ColorFilter.tint(Emerald)
                            )
                        }
                    }
                }
            }
        }
    }

    Box (
        modifier = Modifier
            .offset (
                x = with(density) { imagePosition.x.toDp() },
                y = with(density) { imagePosition.y.toDp() }
            )
            .size (
                width = with(density) { imageSize.height.toDp() },
                height = with(density) { imageSize.width.toDp() }
            )
            .background (
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image (
            painter = painterResource(R.mipmap.applogo_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
}
