package com.example.financeapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun EditGoalMenu(expanded: Boolean, onDismissRequest: () -> Unit, onNewAmount: (String) -> Unit, onSaved: (String) -> Unit, currentGoalText: String) {

    var isEditing by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf("500.50") }
    val originalAmountText by remember { mutableStateOf(amountText) } //einfach nur der Erinnerungswert, falls das Editieren verworfen wird

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = {
            isEditing = false
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
                if (isEditing) {
                    isEditing = false
                    amountText = originalAmountText
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
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "Amount: ",
                    color = Color.White,
                    fontSize = 24.sp
                )

                Text (
                    text = "10000.50",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {

                        }
                )
            }

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
                Text (
                    text = "Already saved: ",
                    color = Color.White,
                    fontSize = 24.sp,
                )

                if (isEditing) {

                    TextField (
                        value = amountText,
                        onValueChange = { newText ->
                            amountText = newText
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
                                isEditing = true
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

                        if (isEditing) {
                            isEditing = false
                        } else {
                            onDismissRequest()
                        }
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
        }
    }
}