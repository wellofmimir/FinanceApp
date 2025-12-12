package com.example.financeapp.receiptsscreen

import com.example.financeapp.R
import com.example.financeapp.database.Receipt

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Checkbox
import androidx.compose.ui.text.input.KeyboardType
import java.io.File
import androidx.core.content.FileProvider
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.DatePicker
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import android.media.ExifInterface
import android.graphics.Matrix

enum class Timespan (id: Int) {

    NONE (-1),
    THIS_MONTH (0),
    LAST_TWO_MONTHS (1),
    LAST_SIX_MONTHS (2),
    WHOLE_YEAR (3),
    ALL (4)
}

fun Bitmap.fixOrientation(path: String): Bitmap {

    val exif = ExifInterface(path)

    val rotation = when (exif.getAttributeInt (
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    if (rotation == 0f)
        return this

    val matrix = Matrix().apply {
        postRotate(rotation)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddReceiptMenu(expanded: Boolean, onDismissRequest: () -> Unit, onReceiptSaved:() -> Unit, receiptSectionsViewModel: ReceiptSectionsViewModel, context: Context = LocalContext.current) {

    var amountText by remember { mutableStateOf("") }
    var nameOfReceipt by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var photoCanBeTaken by remember { mutableStateOf(false) }

    var takePhotoButtonText by remember { mutableStateOf("Take a photo") }
    var insertSuccessful = receiptSectionsViewModel.insertState.collectAsState()

    var remindMeCheckboxChecked by remember { mutableStateOf(false) }
    var showDatepickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember {mutableStateOf<String>("")}

    val datepickerState = rememberDatePickerState (

        initialSelectedDateMillis = System.currentTimeMillis(),

        selectableDates = object: SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis()
            }
        }
    )

    val resetAndDismiss = {

        amountText = ""
        nameOfReceipt = ""
        errorMessage = ""
        photoCanBeTaken = false
        onDismissRequest()
        onReceiptSaved()
    }

    LaunchedEffect(errorMessage) {

        if (!errorMessage.isEmpty()) {
            delay(3000)
            errorMessage = ""
        }
    }

    LaunchedEffect(insertSuccessful.value) {

        if (expanded) {
            delay(1000)
            resetAndDismiss()
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
                        nameOfReceipt,
                        amountText.toFloat(),
                        it.absolutePath,
                        ""
                    ),
                    selectedDate
                )

                resetAndDismiss()
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

    DropdownMenu (
        expanded,
        onDismissRequest = {
            onDismissRequest()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(425.dp)
            .heightIn(min = 425.dp)
            .background(
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

            Text (
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
                        .padding(start = 2.dp)
                )

                TextField (
                    value = nameOfReceipt,
                    onValueChange = { newText ->
                        nameOfReceipt = newText
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
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
                        .padding(start = 2.dp)
                )

                TextField (
                    value = amountText,
                    onValueChange = { newText ->

                        val moneyRegex = Regex("^\\d+(\\.\\d{0,2})?\$")

                        if (moneyRegex.matches(newText)) {
                            amountText = newText
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

            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text (
                    text = "Remind me",
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f)
                )

                Checkbox (
                    checked = remindMeCheckboxChecked,
                    onCheckedChange = {

                        if (!remindMeCheckboxChecked)
                            showDatepickerDialog = true

                        remindMeCheckboxChecked = it
                    },
                    modifier = Modifier
                        .weight(2f)
                )

                if (showDatepickerDialog) {

                    DatePickerDialog (
                        modifier = Modifier
                            .height(400.dp),
                        onDismissRequest = {
                            showDatepickerDialog = false
                        },
                        confirmButton = {
                            TextButton (
                                onClick = {
                                    val dateMillis = datepickerState.selectedDateMillis

                                    if (dateMillis != null) {
                                        val localDate = Instant.ofEpochMilli(dateMillis)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()

                                        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                                        selectedDate = formatter.format(localDate)
                                    }

                                    showDatepickerDialog = false
                                }
                            ) {
                                Text (
                                    text = "Ok"
                                )
                            }
                        },
                        dismissButton = {
                            TextButton (
                                onClick = {
                                    showDatepickerDialog = false
                                }
                            ) {
                                Text (
                                    text = "Cancel"
                                )
                            }
                        }
                    ) {
                        DatePicker (
                            state = datepickerState
                        )
                    }
                }

            }

            Spacer(
                modifier = Modifier
                    .height(48.dp)
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button (
                    onClick = {
                        if (!photoCanBeTaken) {

                            if (nameOfReceipt.isEmpty()) {
                                errorMessage = "Photo can not be taken yet - give your receipt a name."
                                return@Button
                            }

                            if (nameOfReceipt.length > 30) {
                                errorMessage = "Choose a name a bit shorter."
                                return@Button
                            }

                            if (amountText.isEmpty()) {
                                errorMessage = "Photo can not be taken yet - insert the amount on the receipt."
                                return@Button
                            }

                            photoCanBeTaken = true
                        }

                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.buttonColors (
                        containerColor = Pistachio,
                        contentColor = Emerald
                    ),
                    border = BorderStroke (
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

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!errorMessage.isEmpty()) {
                    Text (
                        text = errorMessage,
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

@Composable
fun SinceWhenSection(modifier: Modifier = Modifier, onCurrentMonth: (timeSpanIndex: Timespan) -> Unit, receiptSectionsViewModel: ReceiptSectionsViewModel) {

    val currentMonth by receiptSectionsViewModel.currentMonth.collectAsState()
    var clickedEntry by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        receiptSectionsViewModel.getCurrentMonth()
    }

    val entries = listOf(currentMonth, "2 mo.", "6 mo.", "1 yr", "All")

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            entries.forEach { entry ->
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            clickedEntry = entries.indexOf(entry)

                            onCurrentMonth(
                                when (clickedEntry) {
                                    0 -> Timespan.THIS_MONTH
                                    1 -> Timespan.LAST_TWO_MONTHS
                                    2 -> Timespan.LAST_SIX_MONTHS
                                    3 -> Timespan.WHOLE_YEAR
                                    4 -> Timespan.ALL
                                    else -> Timespan.NONE
                                }
                            )
                        }
                        .background (
                            color = if (entries.indexOf(entry) == clickedEntry) Emerald else Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border (
                            width = 2.dp,
                            color = if (entries.indexOf(entry) == clickedEntry) Pistachio else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text (
                        text = entry,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Normal,
                        color = if (entries.indexOf(entry) == clickedEntry) Pistachio else Emerald
                    )
                }
            }
        }
    }
}

@Composable
fun AverageSpentSection(modifier: Modifier = Modifier, timespan: Timespan, receiptAdded: () -> Unit, onDismissRequest: () -> Unit, receiptSectionsViewModel: ReceiptSectionsViewModel) {

    when (timespan) {
        Timespan.THIS_MONTH -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_TWO_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfTwoMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_SIX_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfSixMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.WHOLE_YEAR -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfAYearAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.NONE -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.ALL -> receiptSectionsViewModel.getReceipts()
    }

    val receipts by receiptSectionsViewModel.receipts.collectAsState()

    LaunchedEffect(receipts) {
        receiptSectionsViewModel.calculateAverage()
    }

    var expanded by remember { mutableStateOf(false) }
    receiptSectionsViewModel.calculateAverage()
    val averageAmount by receiptSectionsViewModel.receiptsAverage.collectAsState()

    Column (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddReceiptMenu (
            expanded,
            onDismissRequest = {
                expanded = false
                receiptSectionsViewModel.getReceipts()
            },
            onReceiptSaved = {
                receiptAdded()
            },
            receiptSectionsViewModel
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 18.dp),
            text = averageAmount.toString(),
            color = Emerald,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 2.dp),
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
            painter = painterResource(R.drawable.kamerasymbol_foreground),
            contentDescription = "Kamerasymbol",
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
    }
}

@Composable
fun ExpensesOverviewSection(modifier: Modifier = Modifier, timespan: Timespan, receiptSectionsViewModel: ReceiptSectionsViewModel) {

    when (timespan) {
        Timespan.THIS_MONTH -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_TWO_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfTwoMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_SIX_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfSixMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.WHOLE_YEAR -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfAYearAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.NONE -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.ALL -> receiptSectionsViewModel.getReceipts()
    }

    val timeRangeText = when (timespan) {
        Timespan.THIS_MONTH -> "This month"
        Timespan.LAST_TWO_MONTHS -> "Last two months"
        Timespan.LAST_SIX_MONTHS -> "Last six months"
        Timespan.WHOLE_YEAR -> "Last year"
        Timespan.NONE -> ""
        Timespan.ALL -> "All"
    }

    val receipts by receiptSectionsViewModel.receipts.collectAsState()
    val sumOfExpenses by receiptSectionsViewModel.receiptsSum.collectAsState()

    Column (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 18.dp),
            text = "$timeRangeText:",
            color = Emerald,
            textAlign = TextAlign.Start,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 9.dp),
            text = sumOfExpenses.toString(),
            color = Emerald,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer (
            modifier = Modifier
                .height(5.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text (
                text = receipts.size.toString(),
                color = Emerald,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp, bottom = 10.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text (
                text = "Purchases recorded",
                color = Emerald,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun ReceiptLogSection(modifier: Modifier = Modifier, timespan: Timespan, receiptSectionsViewModel: ReceiptSectionsViewModel) {

    when (timespan) {
        Timespan.THIS_MONTH -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_TWO_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfTwoMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_SIX_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfSixMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.WHOLE_YEAR -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfAYearAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.NONE -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.ALL -> receiptSectionsViewModel.getReceipts()
    }

    val receipts by receiptSectionsViewModel.receipts.collectAsState()
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
                },
                properties = DialogProperties (
                    usePlatformDefaultWidth = false
                )
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image (
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (showDialog) {
                                    showDialog = false
                                }
                            }
                    )
                }
            }
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(
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
                        ) {
                            val file = File(receipt.pathToImage)

                            if (file.exists()) {
                                bitmap = BitmapFactory.decodeFile(file.absolutePath).fixOrientation(file.absolutePath)
                                showDialog = true
                            }
                            
                            //TODO: Behandlung einbauen für den Fall, das file nicht existiert
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text (
                        text = receipt.description,
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(start = 36.dp)
                    )

                    Text (
                        text = receipt.date,
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Text (
                        text = receipt.amount.toString(),
                        color = Emerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

