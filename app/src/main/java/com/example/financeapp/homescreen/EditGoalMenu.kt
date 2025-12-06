package com.example.financeapp.homescreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.example.financeapp.database.Goal
import com.example.financeapp.ui.theme.Pistachio

@Composable

fun EditGoalMenu(expanded: Boolean, goal: Goal?, onDismissRequest: () -> Unit, onNewAmount: (String) -> Unit, onSaved: (String) -> Unit, currentGoalText: String) {

    var isEditingInitialAmount by remember { mutableStateOf(false) }
    var isEditingSavedAmount by remember { mutableStateOf(false) }

    var amountText by remember { mutableStateOf("") }
    var originalAmountText by remember { mutableStateOf(amountText) } //einfach nur der Erinnerungswert, falls das Editieren verworfen wird
    var amountTextAsTextFieldValue by remember { //wird benötigt, um den Cursor ans Ende des TextFields zu setzen bei anclicken
        mutableStateOf(TextFieldValue(originalAmountText))
    }

    var savedAmountText by remember { mutableStateOf("" ) }
    var originalSavedAmountText by remember { mutableStateOf(savedAmountText) }
    var savedAmountTextAsFieldValue by remember { //wird benötigt, um den Cursor ans Ende des TextFields zu setzen bei anclicken
        mutableStateOf(TextFieldValue(originalSavedAmountText))
    }

    var tokenCount by remember { mutableStateOf(5) }
    var hasChanged by remember { mutableStateOf(false) }

    LaunchedEffect(expanded) {
        goal?.let {

            amountText = goal.amount.toString()
            originalAmountText = amountText

            savedAmountText = goal.saved.toString()
            originalSavedAmountText = savedAmountText

            tokenCount = goal.tokenCount
        }
    }

    LaunchedEffect(goal) {
        goal?.let {

            amountText = goal.amount.toString()
            originalAmountText = amountText

            savedAmountText = goal.saved.toString()
            originalSavedAmountText = savedAmountText
        }
    }

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = {

            isEditingSavedAmount = false
            isEditingInitialAmount = false

            amountText = originalAmountText
            savedAmountText = originalSavedAmountText

            onDismissRequest()
        },
        modifier = Modifier
            .fillMaxWidth()
            .background (
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (isEditingSavedAmount) {

                    savedAmountText = originalSavedAmountText

                    isEditingSavedAmount = false
                    isEditingInitialAmount = false
                }

                if (isEditingInitialAmount) {

                    amountText = originalAmountText

                    isEditingSavedAmount = false
                    isEditingInitialAmount = false
                }
            },
        containerColor = Color.Transparent,
    ) {
        Column (
            modifier = Modifier
                .background (
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text (
                text = currentGoalText,
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
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
            }

            Spacer (
                modifier = Modifier
                    .height(8.dp)
            )

            Row (
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald
                    )
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "Amount: ",
                    color = Color.White,
                    fontSize = 24.sp
                )

                val focusRequester = remember { FocusRequester() }
                var hasFocus by remember { mutableStateOf(false) }

                if (isEditingInitialAmount) {

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()

                        amountTextAsTextFieldValue = amountTextAsTextFieldValue.copy (
                            selection = TextRange(originalAmountText.length),
                            text = originalAmountText
                        )
                    }

                    TextField (
                        value = amountTextAsTextFieldValue,
                        onValueChange = { newText ->

                            amountTextAsTextFieldValue = newText
                            amountText = amountTextAsTextFieldValue.text
                            hasChanged = true
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
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->

                                if (focusState.hasFocus) {
                                    isEditingSavedAmount = false

                                    if (!isEditingInitialAmount)
                                        isEditingInitialAmount = true
                                }

                                if (!focusState.hasFocus){

                                    amountTextAsTextFieldValue = TextFieldValue(originalAmountText)
                                    amountText = originalAmountText
                                }
                            }
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                            }
                    )

                } else {

                    Text (
                        text = amountText,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditingInitialAmount = true
                                isEditingSavedAmount = false
                            }
                    )
                }
            }

            Row (
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald
                    )
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "Already saved: ",
                    color = Color.White,
                    fontSize = 24.sp,
                )

                val focusRequester = remember { FocusRequester() }
                var hasFocus by remember { mutableStateOf(false) }

                if (isEditingSavedAmount) {

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()

                        savedAmountTextAsFieldValue = savedAmountTextAsFieldValue.copy (
                            selection = TextRange(originalSavedAmountText.length),
                            text = originalSavedAmountText
                        )
                    }

                    TextField (
                        value = savedAmountTextAsFieldValue,
                        onValueChange = { newText ->

                            savedAmountTextAsFieldValue = newText
                            savedAmountText = savedAmountTextAsFieldValue.text
                            hasChanged = true
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
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->

                                if (focusState.hasFocus) {
                                    isEditingInitialAmount = false

                                    if (!isEditingSavedAmount)
                                        isEditingSavedAmount = true
                                }

                                if (!focusState.hasFocus){

                                    savedAmountTextAsFieldValue = TextFieldValue(originalSavedAmountText)
                                    savedAmountText = originalSavedAmountText
                                }
                            }
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                            }
                    )

                } else {

                    Text (
                        text = savedAmountText,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditingSavedAmount = true
                                isEditingInitialAmount = false
                            }
                    )
                }
            }

            Spacer (
                modifier = Modifier
                    .height(48.dp)
            )

            Row (
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald
                    )
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button (
                    onClick = {
                        if (hasChanged) {

                            isEditingSavedAmount = false
                            isEditingInitialAmount = false

                            onNewAmount(amountText)
                            onSaved(savedAmountText)
                            onDismissRequest()
                        } else {
                            onDismissRequest()
                        }

                        hasChanged = false //resettet alles oben im LaunchedEffect dann
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
                        text = "Save"
                    )
                }
            }

            Spacer (
                modifier = Modifier
                    .height (12.dp)
            )

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .background (
                        color = Emerald,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = when (tokenCount) {
                            1 -> Emerald
                            2 -> Emerald
                            3 -> Emerald
                            4 -> Pistachio
                            5 -> Pistachio
                            else -> Pistachio
                        },
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = when (tokenCount) {
                            1 -> Emerald
                            2 -> Pistachio
                            3 -> Pistachio
                            4 -> Pistachio
                            5 -> Pistachio
                            else -> Pistachio
                        },
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = when (tokenCount) {
                            1 -> Pistachio
                            2 -> Emerald
                            3 -> Pistachio
                            4 -> Pistachio
                            5 -> Pistachio
                            else -> Pistachio
                        },
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = when (tokenCount) {
                            1 -> Emerald
                            2 -> Pistachio
                            3 -> Pistachio
                            4 -> Pistachio
                            5 -> Pistachio
                            else -> Pistachio
                        },
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = when (tokenCount) {
                            1 -> Emerald
                            2 -> Emerald
                            3 -> Emerald
                            4 -> Emerald
                            5 -> Pistachio
                            else -> Emerald
                        },
                        style = Fill
                    )
                }
            }
        }
    }
}