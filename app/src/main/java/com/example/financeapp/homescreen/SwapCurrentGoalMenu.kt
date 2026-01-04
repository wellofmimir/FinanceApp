package com.example.financeapp.homescreen

import com.example.financeapp.R
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource

@Composable
fun SwapCurrentGoalMenu(expanded: Boolean, onDismissRequest: () -> Unit, goalsSectionViewModel: GoalsSectionViewModel) {

    val colors = LocalAppColors.current
    val goals by goalsSectionViewModel.goals.collectAsState()
    val currentGoal by goalsSectionViewModel.currentGoal.collectAsState()

    DropdownMenu (
        expanded = expanded,
        onDismissRequest,
        modifier = Modifier
            .border (
                width = 1.dp,
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
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
                text = "Swap current goal",
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

            goals.take(5).forEach { item ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            shape = RoundedCornerShape (
                                bottomStart = if (item == goals.last()) 12.dp else 0.dp,
                                bottomEnd = if (item == goals.last()) 12.dp else 0.dp
                            ),
                            color = colors.primary
                        )
                        .padding(horizontal = 36.dp, vertical = 8.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember {MutableInteractionSource()}
                        ) {
                            goalsSectionViewModel.setCurrentGoal(item)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val painter = painterResource (
                        id = if (item.id == currentGoal?.id)
                                R.drawable.bulletpointpistachiofilled_foreground
                            else
                                R.drawable.bulletpointpistachio_foreground
                    )

                    Image (
                        painter = painter,
                        contentDescription = "BulletpointPistachio",
                        colorFilter = ColorFilter.tint(colors.secondary),
                        modifier = Modifier
                            .background (
                                color = Color.Transparent
                            )
                            .size(24.dp)
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    Text (
                        fontSize = 18.sp,
                        color = colors.secondary,
                        text = item.goal
                    )
                }
            }
        }
    }
}