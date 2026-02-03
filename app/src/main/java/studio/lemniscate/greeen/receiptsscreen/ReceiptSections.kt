package studio.lemniscate.greeen.receiptsscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.database.Receipt
import studio.lemniscate.greeen.commonutils.fixOrientation
import studio.lemniscate.greeen.commonutils.shareToWhatsapp
import studio.lemniscate.greeen.commonutils.getShareableImageUri
import studio.lemniscate.greeen.commonutils.shareToFacebook
import studio.lemniscate.greeen.commonutils.shareToFacebookMessenger
import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.commonutils.FileProvider.*

import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import java.io.File
import java.math.RoundingMode

import kotlin.toBigDecimal

import android.os.Build
import androidx.core.content.FileProvider
import android.Manifest

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.LaunchedEffect

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.draw.rotate

import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyRow

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.AlertDialog

enum class Timespan (id: Int) {

    NONE (-1),
    THIS_MONTH (0),
    LAST_TWO_MONTHS (1),
    LAST_SIX_MONTHS (2),
    WHOLE_YEAR (3),
    ALL (4)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddReceiptMenu (
    onDismissRequest: () -> Unit,
    onReceiptSaved:() -> Unit,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current

    val expenses by receiptSectionsViewModel.expenses.collectAsState()
    var expenseCategory by remember { mutableStateOf("") }

    var amountText by remember { mutableStateOf("") }
    var nameOfReceipt by remember { mutableStateOf("") }

    var errorTitle by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var photoCanBeTaken by remember { mutableStateOf(false) }

    var takePhotoButtonText by remember { mutableStateOf("Take a photo") }

    var remindMeCheckboxChecked by remember { mutableStateOf(true) }
    var showDatepickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember {mutableStateOf<String>("")}

    val datePickerState = rememberDatePickerState (
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
        remindMeCheckboxChecked = true
        showDatepickerDialog = false
        selectedDate = ""
    }

    if (errorMessage.isNotEmpty()) {
        AlertDialog (
            modifier = Modifier
                .background (
                    color = colors.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(225.dp),
            onDismissRequest = {
                errorMessage = ""
                errorTitle = ""
            },
            title = {
                Text (
                    text = errorTitle,
                    color = colors.secondary
                )
            },
            text = {
                Text (
                    text = errorMessage,
                    color = colors.secondary
                )
            },
            confirmButton = {
                TextButton (
                    modifier = Modifier
                        .border (
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background (
                            color = colors.secondary,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    onClick = {
                        errorMessage = ""
                        errorTitle = ""
                    }
                ) {
                    Text (
                        text = "Okay",
                        color = colors.primary
                    )
                }
            },
            containerColor = colors.primary
        )
    }

    val photos = remember { mutableStateListOf<File>() }

    val takePictureLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val lastPhoto = photos.lastOrNull()

            lastPhoto?.let {
                receiptSectionsViewModel.insertReceipt (
                    receipt = Receipt (
                        -1,
                        nameOfReceipt,
                        amountText.toFloat(),
                        it.absolutePath,
                        "",
                        "",
                        expenseCategory
                    ),
                    remindMeDate = selectedDate
                )

                receiptSectionsViewModel.showToast("Receipt saved.")
                resetAndDismiss()
            }
        }
    }

    // Permission prüfen
    val permissionLauncher = rememberLauncherForActivityResult (
         contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {

            val fileProvider = studio.lemniscate.greeen.commonutils.FileProvider(context)
            val photoFile = fileProvider.getPhoto()

            //Dieser FileProvider kommt von Android und nicht von mir
            val photoUri = FileProvider.getUriForFile(context, "studio.lemniscate.greeen.provider", photoFile)
            photos.add(photoFile)

            takePictureLauncher.launch(photoUri)

        } else {
            receiptSectionsViewModel.showToast("Permission for Camera needed.")
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (!granted) {
            receiptSectionsViewModel.showToast("We can't remind you without permission. :(")
        }

        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= 33)
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .border (
                width = 1.dp,
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer (
            modifier = Modifier
                .height(100.dp)
        )

        Text (
            text = "Add a receipt",
            color = colors.textSecondary,
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
                .height(24.dp)
        )

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text (
                text = "Name",
                color = colors.textSecondary,
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
                colors = TextFieldDefaults.colors (
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = colors.textSecondary,
                    focusedIndicatorColor = colors.textSecondary,
                    cursorColor = colors.textSecondary,
                    focusedTextColor = colors.textSecondary,
                    unfocusedTextColor = colors.textSecondary
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
                color = colors.textSecondary,
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
                    unfocusedIndicatorColor = colors.textSecondary,
                    focusedIndicatorColor = colors.textSecondary,
                    cursorColor = colors.textSecondary,
                    focusedTextColor = colors.textSecondary,
                    unfocusedTextColor = colors.textSecondary
                ),
                keyboardOptions = KeyboardOptions (
                    keyboardType = KeyboardType.Number
                )
            )
        }

        Spacer (
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
                color = colors.textSecondary,
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
                                val dateMillis = datePickerState.selectedDateMillis

                                if (dateMillis != null) {
                                    val localDate = Instant.ofEpochMilli(dateMillis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()

                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
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
                        state = datePickerState
                    )
                }
            }

        }

        Spacer (
            modifier = Modifier
                .height(24.dp)
        )

        LazyRow (
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .size(50.dp)
                .border (
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = colors.secondary
                )
        ) {
            items(expenses) { expense ->
                Box (
                    modifier = Modifier
                        .background (
                            color = if (expenseCategory == expense.category) colors.secondary else colors.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable (
                        ) {
                            expenseCategory = expense.category
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text (
                        text = expense.category,
                        color = if (expenseCategory == expense.category) colors.primary else colors.secondary,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer (
            modifier = Modifier
                .height(36.dp)
        )

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button (
                onClick = {
                    if (!photoCanBeTaken) {

                        if (nameOfReceipt.isEmpty()) {
                            errorTitle = "Give it a name!"
                            errorMessage = "Photo can not be taken yet - give your receipt a name."
                            return@Button
                        }

                        if (nameOfReceipt.length > 40) {
                            errorTitle = "A bit shorter."
                            errorMessage = "Choose a name a bit shorter."
                            return@Button
                        }

                        if (amountText.isEmpty()) {
                            errorTitle = "How much?"
                            errorMessage = "Photo can not be taken yet - insert the amount on the receipt."
                            return@Button
                        }

                        if (expenseCategory.isEmpty()) {
                            errorTitle = "What category?"
                            errorMessage = "Photo can not be taken yet - choose a category for your expense."
                            return@Button
                        }

                        photoCanBeTaken = true
                    }

                    if (remindMeCheckboxChecked) {
                        if (selectedDate.isEmpty()) {
                            showDatepickerDialog = true
                            return@Button
                        }
                    }

                    if (Build.VERSION.SDK_INT >= 33)
                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                },
                colors = ButtonDefaults.buttonColors (
                    containerColor = colors.surface,
                    contentColor = colors.background
                ),
                border = BorderStroke (
                    width = 1.dp,
                    color = colors.textSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text (
                    text = takePhotoButtonText,
                    textAlign = TextAlign.Center,
                    color = colors.background,
                    fontSize = 18.sp
                )
            }
        }

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer (
                modifier = Modifier
                    .height(36.dp)
            )

            Image (
                painter = painterResource(R.drawable.pfeilnachrechts_foreground),
                contentDescription = "PfeilNachRechts",
                colorFilter = ColorFilter.tint(colors.secondary),
                modifier = Modifier
                    .rotate(-180f)
                    .size(64.dp)
                    .weight(1f)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDismissRequest()
                    }
            )
        }
    }
}

@Composable
fun SinceWhenSection (
    modifier: Modifier = Modifier,
    onCurrentMonth: (timeSpanIndex: Timespan) -> Unit,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val currentMonth by receiptSectionsViewModel.currentMonth.collectAsState()
    var clickedEntry by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        receiptSectionsViewModel.getCurrentMonth()
    }

    val entries = listOf(currentMonth, "2 mo.", "6 mo.", "1 yr", "All")

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f),
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
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (tutorialInformation.isActive)
                                return@clickable

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
                            color = if (entries.indexOf(entry) == clickedEntry) colors.background else colors.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border (
                            width = 2.dp,
                            color = if (entries.indexOf(entry) == clickedEntry) colors.surface else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text (
                        text = entry,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Normal,
                        color = if (entries.indexOf(entry) == clickedEntry) colors.surface else colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun AverageSpentSection (
    modifier: Modifier = Modifier,
    timespan: Timespan,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current

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

    receiptSectionsViewModel.calculateAverage()
    val averageAmount by receiptSectionsViewModel.receiptsAverage.collectAsState()

    val currency by receiptSectionsViewModel.currency.collectAsState()
    receiptSectionsViewModel.getCurrency()

    val textSize = when (averageAmount) {
        in 0f..9f -> 26.sp
        in 10f..99f -> 26.sp
        in 100f..999f -> 26.sp
        in 1000f..9999f -> 26.sp
        in 10000f..99999f -> 26.sp
        in 100000f..999999f -> 26.sp
        else -> if (currency.length == 1) 26.sp else 24.sp
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 18.dp),
            text = if (currency.length == 1) currency + " " + averageAmount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() else averageAmount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() + " " + currency,
            color = colors.textPrimary,
            fontSize = textSize,
            fontWeight = FontWeight.Bold
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 2.dp),
            text = "Average Spent Per Transaction",
            color = colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer (
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun ExpensesOverviewSection (
    modifier: Modifier = Modifier,
    timespan: Timespan,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current

    when (timespan) {
        Timespan.THIS_MONTH -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_TWO_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfTwoMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.LAST_SIX_MONTHS -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfSixMonthsAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.WHOLE_YEAR -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfAYearAgo(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.NONE -> receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
        Timespan.ALL -> receiptSectionsViewModel.getReceipts()
    }

    val timeRangeText = when (timespan) {
        Timespan.THIS_MONTH -> "This Month"
        Timespan.LAST_TWO_MONTHS -> "Last Two Months"
        Timespan.LAST_SIX_MONTHS -> "Last Six Months"
        Timespan.WHOLE_YEAR -> "Last Year"
        Timespan.NONE -> ""
        Timespan.ALL -> "All"
    }

    val receipts by receiptSectionsViewModel.receipts.collectAsState()
    val sumOfExpenses by receiptSectionsViewModel.receiptsSum.collectAsState()
    val currency by receiptSectionsViewModel.currency.collectAsState()
    receiptSectionsViewModel.getCurrency()

    val textSize = when (sumOfExpenses) {
        in 0f..9f -> 26.sp
        in 10f..99f -> 26.sp
        in 100f..999f -> 26.sp
        in 1000f..9999f -> 26.sp
        in 10000f..99999f -> 26.sp
        in 100000f..999999f -> 26.sp
        else -> if (currency.length == 1) 26.sp else 24.sp
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_SUM_SECTION) 0.1f else 1.0f)
            .background (
                color = colors.surface,
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
            color = colors.textPrimary,
            textAlign = TextAlign.Start,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic
        )

        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, top = 9.dp),
            text = if (currency.length == 1) currency + " " + sumOfExpenses.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() else sumOfExpenses.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() + " " + currency,
            color = colors.textPrimary,
            fontSize = textSize,
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
                color = colors.textPrimary,
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
                text = "Purchases Recorded",
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReceiptLogSection (
    modifier: Modifier = Modifier,
    timespan: Timespan,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current

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

    val currency by receiptSectionsViewModel.currency.collectAsState()
    receiptSectionsViewModel.getCurrency()

    var currentReceipt by remember { mutableStateOf<Receipt?>(null) }

    var whatsAppClicked by remember { mutableStateOf(false) }
    var facebookClicked by remember { mutableStateOf(false) }
    var facebookMessengerClicked by remember {mutableStateOf(false)}

    LaunchedEffect(whatsAppClicked) {
        receiptSectionsViewModel.shareEventForWhatsApp.collect { event ->
            when (event) {
                is ShareEvent.SharedReceipt -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToWhatsapp(context, uri, event.text)
                    whatsAppClicked = false
                }
            }
        }
    }

    LaunchedEffect(facebookClicked) {
        receiptSectionsViewModel.shareEventForFacebook.collect { event ->
            when (event) {
                is ShareEvent.SharedReceipt -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToFacebook(context, uri)
                    facebookClicked = false
                }
            }
        }
    }

    LaunchedEffect(facebookMessengerClicked) {
        receiptSectionsViewModel.shareEventForFacebookMessenger.collect { event ->
            when (event) {
                is ShareEvent.SharedReceipt -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToFacebookMessenger(context, uri)
                    facebookMessengerClicked = false
                }
            }
        }
    }

    if (showDialog && bitmap != null) {
        Dialog (
            onDismissRequest = {
                showDialog = false
            },
            properties = DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image (
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (showDialog) {
                                showDialog = false
                            }
                        }
                )

                Spacer (
                    modifier = Modifier
                        .padding(4.dp)
                )

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border (
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = colors.secondary
                        )
                        .background (
                            shape = RoundedCornerShape(12.dp),
                            color = colors.background
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp)
                            .background (
                                color = colors.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        currentReceipt?.let { currentReceipt ->
                            Text (
                                text = currentReceipt.description,
                                color = colors.secondary,
                                fontSize = 24.sp
                            )

                            Text (
                                text = if (currency.length == 1) currency + " " + currentReceipt.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() else currentReceipt.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() + " " + currency,
                                color = colors.secondary
                            )

                            Text (
                                text = currentReceipt.category,
                                color = colors.secondary
                            )

                            if (currentReceipt.remindMeDate.isNotEmpty()) {
                                Text (
                                    text = "When to remind you:\n" + currentReceipt.remindMeDate,
                                    color = colors.secondary
                                )
                            }

                            Spacer (
                                modifier = Modifier
                                    .height(6.dp)
                            )

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .size(200.dp)
                            ) {
                                Box (
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image (
                                        painter = painterResource(R.drawable.whatsapp_foreground),
                                        contentDescription = "Whatsapp-Logo",
                                        colorFilter = ColorFilter.tint(colors.secondary),
                                        modifier = Modifier
                                            .clickable (
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                    return@clickable

                                                receiptSectionsViewModel.shareReceiptOnWhatsApp(currentReceipt)
                                                whatsAppClicked = true
                                            }
                                    )
                                }

                                Box (
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image (
                                        painter = painterResource(R.drawable.facebook_foreground),
                                        contentDescription = "Facebook-Logo",
                                        colorFilter = ColorFilter.tint(colors.secondary),
                                        modifier = Modifier
                                            .clickable (
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                    return@clickable

                                                receiptSectionsViewModel.shareReceiptOnFacebook(currentReceipt)
                                                facebookClicked = true
                                            }
                                    )
                                }

                                Box (
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image (
                                        painter = painterResource(R.drawable.facebookmessenger_foreground),
                                        contentDescription = "Facebook-Messenger",
                                        colorFilter = ColorFilter.tint(colors.secondary),
                                        modifier = Modifier
                                            .clickable (
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                    return@clickable

                                                receiptSectionsViewModel.shareReceiptOnFacebookMessenger(currentReceipt)
                                                facebookMessengerClicked = true
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_LOG_SECTION) 0.1f else 1.0f)
            .background (
                color = colors.surface,
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
            color = colors.textPrimary,
            textAlign = TextAlign.Start,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(receipts.take(receipts.size)) { receipt ->
                var menuOpen by remember { mutableStateOf(false) }

                Row (
                    modifier = Modifier
                        .combinedClickable (
                            onClick = {
                                if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_LOG_SECTION)
                                    return@combinedClickable

                                val file = File(receipt.pathToImage)

                                if (file.exists()) {
                                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                        .fixOrientation(file.absolutePath)

                                    showDialog = true
                                    currentReceipt = receipt
                                }

                                //TODO: Behandlung einbauen für den Fall, das file nicht existiert
                            },
                            onLongClick = {
                                menuOpen = true
                            }
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text (
                        text = receipt.description,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(start = 36.dp)
                    )

                    Text (
                        text = receipt.date,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Text (
                        text = if (currency.length == 1) currency + " " + receipt.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() else receipt.amount.toBigDecimal().setScale(2, RoundingMode.DOWN).toPlainString() + " " + currency,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                DropdownMenu (
                    modifier = Modifier
                        .background (
                            color = colors.primary
                        ),
                    expanded = menuOpen,
                    onDismissRequest = {
                        menuOpen = false
                    }
                ) {
                    DropdownMenuItem (
                        modifier = Modifier
                            .background (
                                color = colors.primary,
                            ),
                        text = {
                            Text (
                                text = "Delete",
                                color = colors.secondary
                            )
                        },
                        onClick = {
                            menuOpen = false
                            receiptSectionsViewModel.deleteReceipt(receipt)
                        }
                    )
                }
            }
        }
    }
}

