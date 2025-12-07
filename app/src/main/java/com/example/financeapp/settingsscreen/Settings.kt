package com.example.financeapp.settingsscreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun SettingsSection(modifier: Modifier = Modifier, headerSectionViewModel: HeaderSectionViewModel, context: Context = LocalContext.current) {

    val settingsViewModel: SettingsViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                val database = FinanceAppDatabase.getInstance(context)
                val repository = FeedbackRepository.getInstance(database)
                return SettingsViewModel(repository) as T
            }
        }
    )

    val user by headerSectionViewModel.user.collectAsState()
    var newUsername by remember { mutableStateOf(user) }

    var feedbackText by remember { mutableStateOf("") }
    val isFeedbackAlreadySent by settingsViewModel.isFeedbackAlreadySent.collectAsState()
    settingsViewModel.feedbackAlreadySent()

    var textAfterFeedbackButtonClicked by remember { mutableStateOf("Thank you for your feedback!") }
    var isEditingTheName by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(100.dp)
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
                    color = Emerald,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
            }

            val focusManager = LocalFocusManager.current

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
                                color = Pistachio
                            )
                    )
                } else {
                    Text (
                        text = user,
                        color = Emerald,
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditingTheName = true
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
                    Image (
                        painter = painterResource(R.drawable.checkhook_foreground),
                        contentDescription = "CheckHook",
                        modifier = Modifier
                            .size(56.dp)
                            .clickable(
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
                .height(4.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(100.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box (
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text (
                    text = "Version Info:",
                    color = Emerald,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
            }

            Box (
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text (
                    text = "1.0.0.0",
                    color = Emerald,
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp
                )
            }
        }

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        if (!isFeedbackAlreadySent) {

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .height(100.dp)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                ) {
                    Text(
                        text = "Feedback",
                        color = Emerald,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp
                    )
                }

                var labelText by remember { mutableStateOf("We love to hear from you! Please let us know how we can improve here ...") }

                OutlinedTextField (
                    value = feedbackText,
                    onValueChange = {
                        feedbackText = it
                    },
                    label = {
                        Text(
                            text = labelText,
                            color = Emerald
                        )
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                        .background(
                            color = Pistachio
                        )
                )

                Box (
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(end = 12.dp)
                        .background(
                            color = Emerald,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            color = Pistachio,
                            width = 2.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .height(40.dp)
                        .width(120.dp)
                        .align(Alignment.End)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            settingsViewModel.sendFeedback(user, feedbackText)

                            labelText = ""
                            feedbackText = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Send",
                        fontSize = 18.sp,
                        color = Pistachio,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                }
            }
        } else {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = textAfterFeedbackButtonClicked,
                    fontSize = 32.sp,
                    color = Emerald,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}