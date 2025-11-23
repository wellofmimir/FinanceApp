package com.example.financeapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import java.io.File
import androidx.core.content.FileProvider
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.clip


@Composable
fun AddReceiptMenu(expanded: Boolean, onDismissRequest: () -> Unit, context: Context = LocalContext.current) {

    val receiptSectionsViewModel: ReceiptSectionsViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = ReceiptRepository.getInstance(database)

                return ReceiptSectionsViewModel(repository) as T
            }
        }
    )

    var takePhotoButtonText by remember { mutableStateOf("Take a photo") }
    var insertSuccessful = receiptSectionsViewModel.insertState.collectAsState()

    LaunchedEffect(insertSuccessful.value) {

        if (expanded) {

            when (insertSuccessful.value) {
                false -> takePhotoButtonText = "X"
                true -> takePhotoButtonText = "✓"
            }

            delay(1000)
            onDismissRequest()
            takePhotoButtonText = ""
            delay(500)
            takePhotoButtonText = "Take a photo"
        }
    }

    val photos = remember { mutableStateListOf<File>() }

    val takePictureLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val lastPhoto = photos.lastOrNull()

            lastPhoto?.let {

                Toast.makeText(context, "Receipt saved: ${it.absolutePath}", Toast.LENGTH_LONG).show()

                receiptSectionsViewModel.insertReceipt (
                    Receipt (
                        -1,
                        "Home depot",
                        250.50f,
                        it.absolutePath
                    )
                )
            }
        }
    }

    // Permission prüfen
    val permissionLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {

            val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            val photoUri = FileProvider.getUriForFile(context, "com.example.financeapp.provider", photoFile)
            photos.add(photoFile)

            takePictureLauncher.launch(photoUri)

        } else {
            Toast.makeText(context, "Permission for camera needed", Toast.LENGTH_SHORT).show()
        }
    }

    var amountText by remember { mutableStateOf("") }
    var amount by remember { mutableFloatStateOf(0.0f) }
    var nameOfReceipt by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    DropdownMenu (
        expanded,
        onDismissRequest = {
            onDismissRequest()
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .background (
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer (
                modifier = Modifier
                    .height(16.dp)
            )

            Text(
                text = "Add a receipt",
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
                    .height(24.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text (
                    text = "Name",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                )

                TextField (
                    value = nameOfReceipt,
                    onValueChange = { newText ->
                        nameOfReceipt = newText
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
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                )

                TextField (
                    value = amountText,
                    onValueChange = { newText ->

                        val moneyRegex = Regex("^\\d+(\\.\\d{0,2})?\$")

                        if (moneyRegex.matches(newText)) {
                            amountText = newText
                            amount = newText.toFloatOrNull() ?: 0f
                        }

                        if (newText.isEmpty())
                            amountText = ""
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
                    keyboardOptions = KeyboardOptions (
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            Spacer (
                modifier = Modifier
                    .height(48.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.buttonColors (
                        containerColor = Pistachio,
                        contentColor = Emerald
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Text (
                        text = takePhotoButtonText,
                        textAlign = TextAlign.Center,
                        color = Emerald,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SinceWhenSection(modifier: Modifier = Modifier, context: Context  = LocalContext.current) {

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val entries = listOf("Oct", "2 mo.", "6 mo.", "1 yr")

        entries.forEach {
            Box (
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .background(
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = it,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    color = Emerald
                )
            }
        }
    }
}

@Composable
fun AverageSpentSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp, top = 18.dp),
            text = "$32,75",
            color = Emerald,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp, top = 2.dp),
            text = "Average spent per transaction",
            color = Emerald,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer (
            modifier = Modifier
                .weight(1f)
        )

        Image (
            painter = painterResource(com.example.financeapp.R.drawable.pluszeichen_foreground),
            contentDescription = "Pluszeichen",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 12.dp, bottom = 12.dp)
                .size(64.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    expanded = true
                },
            alignment = Alignment.BottomEnd
        )

        AddReceiptMenu (
            expanded,
            onDismissRequest = {
                expanded = false
            }
        )
    }
}

@Composable
fun ExpensesOverviewSection(modifier: Modifier = Modifier) {

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = "This month "
        )
    }
}

@Composable
fun ReceiptLogSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    val receiptSectionsViewModel: ReceiptSectionsViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = ReceiptRepository.getInstance(database)

                return ReceiptSectionsViewModel(repository) as T
            }
        }
    )

    val receipts by receiptSectionsViewModel.receipts.collectAsState()
    receiptSectionsViewModel.getReceipts()

    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column (
        modifier = modifier
            .background(
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp, top = 18.dp),
            text = "Receipt log",
            color = Emerald,
            textAlign = TextAlign.Start,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        if (showDialog && bitmap != null) {
            Dialog (
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image (
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(receipts.take(receipts.size)) { receipt ->
                Row (
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            val file = File(receipt.pathToImage)

                            if (file.exists()) {
                                bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                showDialog = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    Text (
                        text = receipt.description,
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Text (
                        text = receipt.date,
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Text (
                        text = receipt.amount.toString(),
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

