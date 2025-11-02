package com.example.financeapp

import android.content.Context
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.window.Popup
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.IntOffset
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio


@Composable
fun FirstGoalMenu(expanded: Boolean, onDismissRequested: () -> Unit) {

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = onDismissRequested,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            ),
        containerColor = Color.Transparent
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
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
                var nameText by remember { mutableStateOf("") }

                TextField (
                    value = nameText,
                    onValueChange = {
                        nameText = it
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
                    .background (
                        color = Emerald
                    )
                    .padding(4.dp)
            )

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
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background (
                        color = Pistachio
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                TextField (
                    value = "",
                    onValueChange = {
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
                        .clickable (
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
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background (
                        color = Pistachio,
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {

                TextField (
                    value = "",
                    onValueChange = {
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
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                        }
                        .padding(start = 10.dp, end = 10.dp)
                )
            }
        }

        if (expanded) {
            Box (
                modifier = Modifier
                    .background(
                        color = Emerald,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                    ) {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_0"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring0_1"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_2"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring0_3"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_4"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring0_5"
                        )
                    }

                    Row(
                    ) {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring1_0"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitdickemrand_foreground),
                            contentDescription = "Ring1_1"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_2"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_3"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .alpha(0f),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            contentDescription = "Ring1_4"
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.pfeilnachrechtspistachio_foreground),
                            contentDescription = "Ring1_5",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen() {

    val density = LocalDensity.current

    var imagePosition by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    var expanded by remember { mutableStateOf(false) }

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
                    expanded = expanded,
                    onDismissRequested = {
                        expanded = false
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
                    text = "Welcome to Greeen.",
                    color = Pistachio,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }

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
                            expanded = true
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
                            contentDescription = "PfeilNachRechts"
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .offset(
                x = with(density) { imagePosition.x.toDp() },
                y = with(density) { imagePosition.y.toDp() }
            )
            .size(
                width = with(density) { imageSize.height.toDp() },
                height = with(density) { imageSize.width.toDp() }
            )
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "LOGO",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}