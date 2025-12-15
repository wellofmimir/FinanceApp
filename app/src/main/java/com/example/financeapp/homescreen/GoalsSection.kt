package com.example.financeapp.homescreen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.homescreen.GoalsSectionViewModel
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep

@Composable
fun GoalsSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, goalsSectionViewModel: GoalsSectionViewModel, context: Context = LocalContext.current) {

    val goals by goalsSectionViewModel.goals.collectAsState()
    var newGoalEntered by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.CURRENT_GOALS) 0.1f else 1.0f)
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text (
                text = "Stuff you're working on:",
                color = Emerald,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = Pistachio
                    )
                    .padding(start = 24.dp, top = 24.dp, end = 0.dp, bottom = 0.dp)
                    .weight(2f)
            )

            Box (
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image (
                    painter = painterResource(R.drawable.pluszeichen_foreground),
                    contentDescription = "Pluszeichen",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){
                            if (tutorialInformation.isActive)
                                return@clickable

                            expanded = true
                            visible = true
                        }
                )
            }

            this@Row.AnimatedVisibility (
                visible = visible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                AddNewGoalMenu (
                    expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    onFinished = {
                        expanded = false
                        newGoalEntered = true
                        goalsSectionViewModel.reloadGoals()
                    },
                    goalsSectionViewModel = goalsSectionViewModel
                )
            }
        }

        LaunchedEffect(Unit) {
            goalsSectionViewModel.reloadGoals()
        }

        if (newGoalEntered) {

            val listState = rememberLazyListState()

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp)
                    .background (
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState
            ) {
                items(goals.take(goals.size)) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image (
                            painter = painterResource(R.drawable.bulletpoint_foreground),
                            contentDescription = "Bulletpoint",
                            modifier = Modifier
                                .background (
                                    color = Pistachio
                                )
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(Emerald)
                        )

                        Spacer (
                            modifier = Modifier
                                .width(24.dp)
                        )

                        Text (
                            text = it.goal,
                            color = Emerald,
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}