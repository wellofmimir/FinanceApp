package com.example.financeapp.settingsscreen
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.ui.graphics.Color
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.header.HeaderSectionViewModel
import com.example.financeapp.R
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.FeedbackRepository
import com.example.financeapp.repositories.CurrencyRepository
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material3.TextButton
import com.example.financeapp.ui.theme.Emerald

@Composable
fun SettingsSection(modifier: Modifier = Modifier, headerSectionViewModel: HeaderSectionViewModel, settingsViewModel: SettingsViewModel, context: Context = LocalContext.current, focusManager: FocusManager = LocalFocusManager.current) {

    val colors = LocalAppColors.current

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val user by headerSectionViewModel.user.collectAsState()
    var newUsername by remember { mutableStateOf(user) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var feedbackTextFieldIsFocused by remember { mutableStateOf(false) }
    var labelText by remember { mutableStateOf("We love to hear from you! Please let us know how we can improve here ...") }
    var feedbackText by remember { mutableStateOf("") }
    val isFeedbackAlreadySent by settingsViewModel.isFeedbackAlreadySent.collectAsState()

    LaunchedEffect(Unit) {
        settingsViewModel.isFeedBackSent()
    }

    var textAfterFeedbackButtonClicked by remember { mutableStateOf("Thank you for your feedback!") }
    var isEditingTheName by remember { mutableStateOf(false) }

    val currency by settingsViewModel.currency.collectAsState()
    settingsViewModel.getCurrency()
    var newCurrency by remember { mutableStateOf(currency) }

    var isEditingTheCurrency by remember { mutableStateOf(false) }

    LaunchedEffect(feedbackTextFieldIsFocused) {
        if (feedbackTextFieldIsFocused) {
            focusRequester.requestFocus()
        }
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(if (feedbackTextFieldIsFocused) 0.dp else 100.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box (
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text (
                    text = "Name:",
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
            }

            Box (
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isEditingTheName) {

                    TextField (
                        value = newUsername,
                        onValueChange = {
                            newUsername = it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background (
                                color = Color.White
                            )
                    )
                } else {
                    Text (
                        text = user,
                        color = colors.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditingTheName = true
                                isEditingTheCurrency = false
                            }
                    )
                }
            }

            if (isEditingTheName) {
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(R.drawable.checkhook_foreground),
                        contentDescription = "CheckHook",
                        modifier = Modifier
                            .size(56.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (isEditingTheName) {
                                    isEditingTheName = false
                                    headerSectionViewModel.updateUser(newUsername)
                                    headerSectionViewModel.getUser()
                                }
                            }
                    )
                }
            }
        }

        Spacer (
            modifier = Modifier
                .height(if (feedbackTextFieldIsFocused) 0.dp else 4.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(if (feedbackTextFieldIsFocused) 0.dp else 100.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box (
                modifier = Modifier
                    .weight(0.75f)
            ) {
                Text (
                    text = "Currency:",
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
            }

            Box (
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(0.5f)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isEditingTheCurrency) {
                    TextField (
                        value = newCurrency,
                        onValueChange = {
                            if (it.length <= 3) {
                                newCurrency = it
                                newCurrency = newCurrency.uppercase()
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .width(200.dp)
                            .padding(10.dp)
                            .background (
                                color = Color.White
                            )
                    )
                } else {
                    Text (
                        text = currency,
                        color = colors.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditingTheCurrency = true
                                isEditingTheName = false
                            }
                    )
                }
            }

            if (isEditingTheCurrency) {
                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(R.drawable.checkhook_foreground),
                        contentDescription = "CheckHook",
                        modifier = Modifier
                            .size(56.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (isEditingTheCurrency) {
                                    isEditingTheCurrency = false

                                    if (newCurrency.isEmpty())
                                        return@clickable

                                    settingsViewModel.setCurrency(newCurrency)
                                    settingsViewModel.getCurrency()
                                }
                            }
                    )
                }
            }
        }

        Spacer (
            modifier = Modifier
                .height(if (feedbackTextFieldIsFocused) 0.dp else 4.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(if (feedbackTextFieldIsFocused) 0.dp else 100.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text(
                    text = "Version Info:",
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "1.0.0.0",
                    color = colors.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp
                )
            }
        }

        Spacer (
            modifier = Modifier
                .height(if (feedbackTextFieldIsFocused) 0.dp else 4.dp)
        )

        if (!isFeedbackAlreadySent) {
            if (showErrorDialog) {
                AlertDialog (
                    modifier = Modifier
                        .background (
                            color = colors.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .height(225.dp),
                    onDismissRequest = {
                        showErrorDialog = false
                    },
                    title = {
                        Text (
                            text = "A bit more, please ...",
                            color = colors.secondary
                        )
                    },
                    text = {
                        Text (
                            text = "Please give us a little bit more elaborate feedback ... :)",
                            color = colors.secondary
                        )
                    },
                    confirmButton = {
                        TextButton (
                            onClick = {
                                showErrorDialog = false
                            }
                        ) {
                            Text (
                                text = "Okay",
                                color = colors.secondary
                            )
                        }
                    },
                    containerColor = colors.primary
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (feedbackTextFieldIsFocused) 0.5f else 0.8f)
                    .background (
                        color = colors.secondary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .height(100.dp)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                Box (
                    modifier = Modifier
                        .weight(0.1f)
                ) {
                    Text (
                        text = "Feedback",
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp
                    )
                }

                OutlinedTextField (
                    value = feedbackText,
                    onValueChange = {
                        feedbackText = it
                    },
                    label = {
                        Text (
                            text = labelText,
                            color = colors.primary
                        )
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                        .background (
                            color = colors.secondary
                        )
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                isEditingTheName = false
                                isEditingTheCurrency = false
                                feedbackTextFieldIsFocused = true
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors (
                            focusedTextColor = Emerald,
                            unfocusedTextColor = Emerald
                        )
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box (
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .background (
                                color = colors.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border (
                                color = colors.secondary,
                                width = 2.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .height(40.dp)
                            .width(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Dismiss",
                            fontSize = 18.sp,
                            color = colors.secondary,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable (
                                ) {
                                    feedbackTextFieldIsFocused = false
                                    feedbackText = ""
                                    focusManager.clearFocus()
                                }
                        )
                    }

                    Box (
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background (
                                color = colors.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border (
                                color = colors.secondary,
                                width = 2.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .height(40.dp)
                            .width(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        var sendButtonText by remember { mutableStateOf("Send") }

                        Text (
                            text = sendButtonText,
                            fontSize = 18.sp,
                            color = colors.secondary,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable (
                                ) {
                                    if (feedbackText.isEmpty() || feedbackText.length < 10) {
                                        showErrorDialog = true
                                        return@clickable
                                    }

                                    sendButtonText = "Wait"
                                    settingsViewModel.sendFeedback(user, feedbackText)
                                    feedbackTextFieldIsFocused = false
                                    sendButtonText = "Send"
                                    keyboardController?.hide()
                                }
                        )
                    }
                }
            }
        } else {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background (
                        color = colors.surface,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = textAfterFeedbackButtonClicked,
                    fontSize = 32.sp,
                    color = colors.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}