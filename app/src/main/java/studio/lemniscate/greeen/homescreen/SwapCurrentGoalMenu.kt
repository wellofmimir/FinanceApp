package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import kotlinx.coroutines.delay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.ColorFilter

import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenuItem
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwapCurrentGoalMenu (
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    goalsSectionViewModel: GoalsSectionViewModel
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    val goals by goalsSectionViewModel.goals.collectAsState()
    val currentGoal by goalsSectionViewModel.currentGoal.collectAsState()
    var menuOpen by remember { mutableStateOf(false) }
    var goalIdToDelete: Int = 0

    val titleText = when(goals.isEmpty()) {
        true -> "No Goals Available"
        false -> "Swap Your Current Goal"
    }

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
                text = titleText,
                color = colors.secondary,
                fontSize = typography.title
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
                        .combinedClickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                goalsSectionViewModel.setCurrentGoal(item)
                            },
                            onLongClick = {
                                menuOpen = true
                                goalIdToDelete = item.id
                            }
                        ),
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
                        fontSize = typography.medium,
                        color = colors.secondary,
                        text = item.goal
                    )
                }
            }
        }

        DropdownMenu (
            modifier = Modifier
                .background (
                    color = colors.secondary
                ),
            expanded = menuOpen,
            onDismissRequest = {
                menuOpen = false
            },
        ) {
            DropdownMenuItem (
                modifier = Modifier
                    .background (
                        color = colors.secondary,
                    ),
                text = {
                    Text(
                        text = "Delete",
                        color = colors.primary
                    )
                },
                onClick = {
                    menuOpen = false
                    goalsSectionViewModel.deleteGoal(goalIdToDelete)
                }
            )
        }
    }
}