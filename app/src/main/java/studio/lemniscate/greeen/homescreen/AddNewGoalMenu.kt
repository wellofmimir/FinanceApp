package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.commonutils.*

import kotlinx.coroutines.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun AddNewGoalMenu (
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onFinished: () -> Unit,
    goalsSectionViewModel: GoalsSectionViewModel
) {
    val colors = LocalAppColors.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var blockInput by remember { mutableStateOf(false) }
    var blockFinish by remember { mutableStateOf(false) }

    var titleText by remember { mutableStateOf("Add a New Goal") }
    var nameOfGoalText by remember { mutableStateOf("") }
    var firstFocusOnGoalText by remember { mutableStateOf(true) }

    var feedbackTrigger by remember { mutableStateOf(0) } // für den LaunchedEffect weiter unten
    var amountText by remember { mutableStateOf("") }
    var firstFocusOnAmountText by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(expanded) {
        if (expanded) {
            nameOfGoalText = ""
            amountText = ""
            firstFocusOnAmountText = true
            firstFocusOnGoalText = true
            errorMessage = null

            blockInput = true
            delay(250)

            var text = "Enter your goal here ..."
            text.forEach {
                nameOfGoalText += it
                delay(50)
            }

            delay(500)

            text = "Enter the amount here ..."
            text.forEach {
                amountText += it
                delay(50)
            }

            blockInput = false
            blockFinish = true
        }
    }

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-100).dp)
    ) {
        DropdownMenu (
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .clip (
                    RoundedCornerShape(12.dp)
                )
                .border (
                    width = 1.dp,
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow (
                    elevation = 12.dp,
                    shape = RoundedCornerShape(12.dp))
                .background (
                    color = colors.primary
                )
                .fillMaxWidth()
                .fillMaxHeight(0.65f),
            containerColor = Color.Transparent

        ) {
            Column (
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = colors.primary
                    )
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = titleText,
                    color = colors.secondary,
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
                    color = colors.secondary
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
                        color = colors.secondary,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                    )

                    val focusRequester = remember { FocusRequester() }

                    TextField (
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (blockInput) {
                                    focusRequester.freeFocus()
                                    return@onFocusChanged
                                }

                                if (it.isFocused && firstFocusOnGoalText) {
                                    nameOfGoalText = ""
                                    firstFocusOnGoalText = false
                                }

                                blockFinish = false
                            },
                        value = nameOfGoalText,
                        onValueChange = { newText ->
                            if (blockInput)
                                return@TextField

                            nameOfGoalText = newText
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors (
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colors.secondary,
                            focusedIndicatorColor = colors.secondary,
                            cursorColor = colors.secondary,
                            focusedTextColor = colors.secondary,
                            unfocusedTextColor = colors.secondary
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
                        color = colors.secondary,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                    )

                    val focusRequester = remember { FocusRequester() }

                    TextField (
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (blockInput) {
                                    focusRequester.freeFocus()
                                    return@onFocusChanged
                                }

                                if (it.isFocused && firstFocusOnAmountText) {
                                    amountText = ""
                                    firstFocusOnAmountText = false
                                }

                                blockFinish = false
                            },
                        value = amountText,
                        onValueChange = { newText ->
                            if (blockInput)
                                return@TextField

                            if (!moneyRegex.matches(newText) && !newText.isBlank())
                                return@TextField

                            amountText = newText
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors (
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colors.secondary,
                            focusedIndicatorColor = colors.secondary,
                            cursorColor = colors.secondary,
                            focusedTextColor = colors.secondary,
                            unfocusedTextColor = colors.secondary
                        ),
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Spacer (
                    modifier = Modifier
                        .height(24.dp)
                )

                var tokenIdentifier by remember { mutableIntStateOf(2) }

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
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                tokenIdentifier = 0
                            }
                    ) {
                        drawCircle (
                            color = colors.secondary,
                            style = Fill
                        )
                    }

                    Canvas (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                tokenIdentifier = 1
                            }
                    ) {
                        drawCircle (
                            color = colors.secondary,
                            style = if (tokenIdentifier >= 1) Fill else Stroke(4f)
                        )
                    }

                    Canvas (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                tokenIdentifier = 2
                            }
                    ) {
                        drawCircle (
                            color = colors.secondary,
                            style = if (tokenIdentifier >= 2) Fill else Stroke(4f)
                        )
                    }

                    Canvas (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                tokenIdentifier = 3
                            }
                    ) {
                        drawCircle (
                            color = colors.secondary,
                            style = if (tokenIdentifier >= 3) Fill else Stroke(4f)
                        )
                    }

                    Canvas (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                tokenIdentifier = 4
                            }
                    ) {
                        drawCircle (
                            color = colors.secondary,
                            style = if (tokenIdentifier >= 4) Fill else Stroke(4f)
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
                            color = colors.primary,
                            shape = RoundedCornerShape(12.dp)
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
                            painter = painterResource(R.drawable.minuszeichen_standard_pistachio_foreground),
                            contentDescription = "Minuszeichen_Token",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (tokenIdentifier in 1..4)
                                        --tokenIdentifier
                                },
                            colorFilter = ColorFilter.tint(colors.secondary)
                        )

                        Spacer (
                            modifier = Modifier
                                .padding(20.dp)
                        )

                        Image (
                            painter = painterResource(R.drawable.pluszeichen_standard_pistachio_foreground),
                            contentDescription = "Pluszeichen_Token",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (tokenIdentifier in 0..< 4)
                                        ++tokenIdentifier
                                },
                            colorFilter = ColorFilter.tint(colors.secondary)
                        )
                    }
                }

                Spacer (
                    modifier = Modifier
                        .padding(8.dp)
                )

                var confirmed by remember { mutableStateOf(false) }

                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button (
                        onClick = {
                            if (blockInput) {
                                errorMessage = "Steady your horses..."
                                return@Button
                            }

                            if (blockFinish || firstFocusOnGoalText || firstFocusOnAmountText) {
                                errorMessage = "Please fill in all fields."
                                return@Button
                            }

                            val inputErrorMessage = validateInput("RandomUsername", nameOfGoalText, amountText)

                            if (inputErrorMessage != null) {
                                errorMessage = inputErrorMessage.message
                                return@Button
                            }

                            keyboardController?.hide()

                            val amountOfTokens: Int = when (tokenIdentifier) {
                                0 -> 1
                                1 -> 2
                                2 -> 3
                                3 -> 4
                                4 -> 5
                                else -> 1
                            }

                            confirmed = true
                            goalsSectionViewModel.insertGoal(nameOfGoalText, amountText.toFloat(), "InProgress", amountOfTokens = amountOfTokens)
                            goalsSectionViewModel.getCurrentGoal() //update für die GoalProgressSection
                            goalsSectionViewModel.reloadGoals()

                            feedbackTrigger++
                        },
                        colors = ButtonDefaults.buttonColors (
                            containerColor = colors.secondary,
                            contentColor = colors.primary
                        ),
                        border = BorderStroke (
                            width = 1.dp,
                            color = colors.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                    ) {
                        Text (
                            text = when {
                                errorMessage != null -> "X"
                                confirmed -> "✓"
                                else -> "Add"
                            }
                        )
                    }

                    LaunchedEffect (feedbackTrigger) {
                        keyboardController?.hide()
                        delay(1000)

                        if (confirmed)
                            onFinished()
                    }
                }

                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (errorMessage != null) {
                        Text (
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}