package com.example.financeapp.welldone
import android.Manifest
import com.example.financeapp.R
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import com.example.financeapp.homescreen.GoalsSectionViewModel
import java.io.File


@Composable
fun QuestionDialog(onConfirm:() -> Unit, onDismissRequest: () -> Unit) {

    val colors = LocalAppColors.current

    AlertDialog (
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
            .height(225.dp),
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text (
                text = "You did it!",
                color = colors.secondary
            )
        },
        text = {
            Text (
                text = "Do you want to snap a photo to remember it?",
                color = colors.secondary
            )
        },
        confirmButton = {
            TextButton (
                onClick = {
                    onConfirm()
                },
                modifier = Modifier
                    .border (
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background (
                        color = colors.secondary,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text (
                    text = "Yes, take a photo",
                    color = colors.primary,

                )
            }
        },
        dismissButton = {
            TextButton (
                onClick = {
                    onDismissRequest()
                },
                modifier = Modifier
                    .border (
                        width = 1.dp,
                        color = colors.secondary,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text (
                    text = "No",
                    color = colors.secondary
                )
            }
        },
        containerColor = colors.primary
    )
}

@Composable
fun WellDoneSection (
    modifier: Modifier = Modifier,
    goalsSectionViewModel: GoalsSectionViewModel,
    idGoal: Int,
    punchCardFilled: Boolean,
    onFinished: () -> Unit,
    context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    var showDialog by remember { mutableStateOf(false) }
    val photos = remember { mutableStateListOf<File>() }

    val takePictureLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photos.lastOrNull()?.let { pathToImage ->
                goalsSectionViewModel.updateImageToGoal(idGoal, pathToImage.absolutePath)
                onFinished()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {

            val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            val photoUri = FileProvider.getUriForFile(context, "com.example.financeapp.provider", photoFile)
            photos.add(photoFile)

            takePictureLauncher.launch(photoUri)

        } else {
            Toast.makeText(context, "Permission for camera needed.", Toast.LENGTH_SHORT).show()
        }
    }

    Column (
        modifier = modifier
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .border (
                width = 2.dp,
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer (
            modifier = Modifier
                .height(50.dp)
        )

        Text (
            text = "Well done!",
            fontSize = 40.sp,
            color = colors.secondary
        )

        Spacer (
            modifier = Modifier
                .height(10.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            for (i in 1 .. 3) {
                Image (
                    painter = painterResource(R.drawable.starfilledpistachio_foreground),
                    contentDescription = "Stern_$i",
                    modifier = Modifier
                        .height(50.dp)
                        .aspectRatio(1f)
                )
            }
        }

        Spacer (
            modifier = Modifier
                .height(20.dp)
        )

        Text (
            text = buildAnnotatedString {
                withStyle (
                    style = SpanStyle (
                        color = colors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    if (punchCardFilled)
                        append("You've filled out the punchcard!")
                    else
                        append ("You've accomplished a goal!")
                }
            },
            fontSize = 16.sp,
            color = colors.secondary,
            textAlign = TextAlign.Center
        )

        Spacer (
            modifier = Modifier
                .height(2.dp)
        )

        Text (
            text = if (punchCardFilled) "Give yourself a BIG treat. You are on a good streak - you've earned it!" else "Now give yourself a little treat. Or a big one. The world is your oyster.",
            fontSize = 16.sp,
            color = colors.secondary,
            textAlign = TextAlign.Center
        )

        Spacer (
            modifier = Modifier
                .height(40.dp)
        )

        Box (
            modifier = Modifier
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
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text (
                text = "I've treated myself!",
                fontSize = 18.sp,
                color = colors.secondary,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
            )
        }

        if (showDialog) {
            QuestionDialog (
                onConfirm = {
                    if (Build.VERSION.SDK_INT >= 33)
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onDismissRequest = {
                    onFinished()
                }
            )
        }
    }
}