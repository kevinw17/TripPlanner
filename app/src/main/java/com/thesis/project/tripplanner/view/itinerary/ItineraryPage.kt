package com.thesis.project.tripplanner.view.itinerary

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.components.DestinationBottomSheet
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.SaveChangesDialog
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryPage(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  authViewModel: AuthViewModel
) {

  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  val context = LocalContext.current
  val profileImageUrl by authViewModel.profileImageUrl.collectAsState()
  val username by authViewModel.username.collectAsState()
  var title by remember { mutableStateOf(Utils.EMPTY) }
  var description by remember { mutableStateOf(Utils.EMPTY) }
  var startDate by remember { mutableStateOf(Utils.EMPTY) }
  var endDate by remember { mutableStateOf(Utils.EMPTY) }
  var selectedDestination by remember { mutableStateOf(emptyList<String>()) }
  val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  var startCalendar by remember { mutableStateOf<Calendar?>(null) }
  var showDialog by remember { mutableStateOf(false) }
  var showOverlay by remember { mutableStateOf(false) }
  val destinationsList by itineraryViewModel.destinations.collectAsState()
  val userId = authViewModel.userId

  val openStartDatePicker = {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
      context,
      { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val selectedDate = Calendar.getInstance().apply {
          set(year, month, dayOfMonth)
        }
        startCalendar = selectedDate
        startDate = dateFormatter.format(selectedDate.time)

        if (endDate.isNotEmpty() && selectedDate.time > dateFormatter.parse(endDate)) {
          endDate = Utils.EMPTY
        }
      },
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
  }

  val openEndDatePicker = {
    val calendar = Calendar.getInstance()
    val minDate = startCalendar?.timeInMillis ?: calendar.timeInMillis
    DatePickerDialog(
      context,
      { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val selectedDate = Calendar.getInstance().apply {
          set(year, month, dayOfMonth)
        }
        endDate = dateFormatter.format(selectedDate.time)
      },
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
      datePicker.minDate = minDate
    }.show()
  }

  LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
    showOverlay = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
  }

  LaunchedEffect(Unit) {
    itineraryViewModel.loadDestinations()
  }

  Scaffold(
    bottomBar = { BottomNavigationBar(navController) }
  ) { innerPadding ->
    BottomSheetScaffold(
      scaffoldState = scaffoldState,
      topBar = {
        TopAppBar(title = {
          Text(
            text = stringResource(R.string.itinerary),
            fontWeight = FontWeight.Bold
          )
        }, navigationIcon = {
          IconButton(
            onClick = {
              navController.popBackStack()
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_arrow_left),
              contentDescription = "Back",
              modifier = Modifier.size(24.dp)
            )
          }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
      },
      sheetContent = {
        DestinationBottomSheet(
          destinations = destinationsList.map { it.name }) { destination ->
            if (destination !in selectedDestination) {
              selectedDestination = selectedDestination + destination
            }
            coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() }
          }
      },
      sheetPeekHeight = 0.dp
    ) { paddingValues ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White)
      ) {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(paddingValues)
            .padding(horizontal = 16.dp), verticalArrangement = Arrangement.Top
        ) {
          item {
            Text(text = stringResource(R.string.buat_itinerary), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))
          }

          item {
            Text(
              text = stringResource(R.string.judul_trip),
              color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              placeholder = { Text(stringResource(R.string.masukkan_judul)) },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
              )
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            Text(
              text = stringResource(R.string.deskripsi),
              color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              placeholder = { Text(stringResource(R.string.masukkan_deskripsi)) },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
              ),
              maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            Text(
              text = stringResource(R.string.tanggal_pergi),
              color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = startDate,
              onValueChange = {},
              placeholder = { Text(stringResource(R.string.pilih_tanggal_pergi)) },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { openStartDatePicker() },
              enabled = false,
              trailingIcon = {
                Icon(
                  painter = painterResource(R.drawable.ic_calendar),
                  contentDescription = "Calendar Icon",
                  modifier = Modifier
                    .size(20.dp)
                    .clickable { openStartDatePicker() })
              },
              colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
                disabledTrailingIconColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Gray,
                disabledLabelColor = Color.Black
              )
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            Text(
              text = stringResource(R.string.tanggal_pulang),
              color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = endDate,
              onValueChange = {},
              placeholder = { Text(stringResource(R.string.pilih_tanggal_pulang)) },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { openEndDatePicker() },
              enabled = false,
              trailingIcon = {
                Icon(
                  painter = painterResource(R.drawable.ic_calendar),
                  contentDescription = "Calendar Icon",
                  modifier = Modifier
                    .size(20.dp)
                    .clickable { openEndDatePicker() })
              },
              colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
                disabledTrailingIconColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Gray,
                disabledLabelColor = Color.Black
              )
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            Text(
              text = stringResource(R.string.tujuan),
              color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = Utils.EMPTY,
              onValueChange = {},
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  showOverlay = true
                  coroutineScope.launch {
                    kotlinx.coroutines.delay(100)
                    scaffoldState.bottomSheetState.expand()
                  }
                },
              enabled = false,
              placeholder = { Text(stringResource(R.string.pilih_destinasi)) },
              trailingIcon = {
                Icon(
                  painter = painterResource(R.drawable.ic_chevron_bottom),
                  contentDescription = "Dropdown Icon",
                  modifier = Modifier.size(20.dp)
                )
              },
              colors = TextFieldDefaults.colors(
                disabledPlaceholderColor = Color.Black,
                disabledTrailingIconColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Gray,
                disabledLabelColor = Color.Black
              )
            )
            LazyRow(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              items(selectedDestination.size) { index ->
                val destination = selectedDestination[index]
                ChipWithRemoveIcon(label = destination, onRemove = {
                  selectedDestination = selectedDestination.toMutableList().apply {
                    remove(destination)
                  }
                })
              }
            }
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            Button(
              onClick = {
                if (title.isBlank() ||
                  description.isBlank() ||
                  startDate.isBlank() ||
                  endDate.isBlank() ||
                  selectedDestination.isEmpty()
                ) {
                  Toast.makeText(
                    context, context.getString(R.string.no_empty_field),
                    Toast.LENGTH_SHORT
                  ).show()
                } else {
                  showDialog = true
                }
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDFF9FF),
                contentColor = Color.Black
              ),
              border = BorderStroke(
                width = 1.dp,
                color = Color.Black
              )
            ) {
              Text(text = stringResource(R.string.simpan), fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
          }
        }
        if (showOverlay) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.Black.copy(alpha = 0.5f))
              .clickable { coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() } }
          )
        }
      }
    }
  }

  if (showDialog) {
    SaveChangesDialog(
      onConfirm = {
        showDialog = false
        userId?.let {
          itineraryViewModel.saveItinerary(
            userId = it,
            username = username,
            profileImageUrl = profileImageUrl.toString(),
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            destinations = selectedDestination
          )
        }
        Toast.makeText(
          context,
          "Rencana perjalanan Anda berhasil dibuat",
          Toast.LENGTH_SHORT
        ).show()
        navController.navigate("home")
      },
      onDismiss = {
        showDialog = false
      }
    )
  }
}

@Composable
fun ChipWithRemoveIcon(label: String, onRemove: () -> Unit) {
  Row(
    modifier = Modifier
      .background(Color(0xFFDFF9FF), shape = RoundedCornerShape(8.dp))
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, fontSize = 14.sp, color = Color.Black)
    Spacer(modifier = Modifier.width(4.dp))
    Icon(
      painter = painterResource(id = R.drawable.ic_close),
      contentDescription = "Remove",
      modifier = Modifier
        .size(8.dp)
        .clickable { onRemove() },
      tint = Color.Black
    )
  }
}