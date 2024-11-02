package com.thesis.project.tripplanner.view.itinerary

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryPage(
  navController: NavController
) {

  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  val context = LocalContext.current
  var selectedDestination by remember { mutableStateOf(Utils.EMPTY) }
  var startDate by remember { mutableStateOf(Utils.EMPTY) }
  var endDate by remember { mutableStateOf(Utils.EMPTY) }
  val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  var startCalendar by remember { mutableStateOf<Calendar?>(null) }

  val destinations = listOf("Jakarta", "Bandung", "Bali", "Surabaya", "Yogyakarta", "Medan", "Makassar")

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

    val datePicker = DatePickerDialog(
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
    )

    datePicker.datePicker.minDate = minDate
    datePicker.show()
  }

//  BottomSheetScaffold(
//    scaffoldState = scaffoldState,
//    sheetContent = {
//      DestinationBottomSheet(
//        destinations = destinations,
//        onSelect = { destination ->
//          selectedDestination = destination
//          coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
//        }
//      )
//    }
//  ) {
    Scaffold(topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.itinerary),
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
      }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }, bottomBar = {
      BottomNavigationBar(navController)
    }) { paddingValues ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(16.dp),
        verticalArrangement = Arrangement.Top
      ) {
        item {
          Text(
            text = stringResource(R.string.buat_itinerary),
            fontSize = 20.sp
          )
          Spacer(modifier = Modifier.height(32.dp))
        }

        item {
          Text(
            text = stringResource(R.string.judul_trip),
            color = Color.Black
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = Utils.EMPTY,
            onValueChange = { /* Handle title input */ },
            placeholder = {
              Text(stringResource(R.string.masukkan_judul))
            },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Text(
            text = stringResource(R.string.deskripsi),
            color = Color.Black
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = "",
            onValueChange = { /* Handle description input */ },
            placeholder = {
              Text(stringResource(R.string.masukkan_deskripsi))
            },
            modifier = Modifier.fillMaxWidth()
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
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White,
              focusedBorderColor = Color.Black,
              unfocusedLabelColor = Color.Black
            ),
            value = startDate,
            onValueChange = {  },
            placeholder = {
              Text(stringResource(R.string.pilih_tanggal_pergi))
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { openStartDatePicker() },
            trailingIcon = {
              Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar Icon",
                modifier = Modifier.clickable { openStartDatePicker() }
              )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Next
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
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = endDate,
            onValueChange = {  },
            placeholder = {
              Text(stringResource(R.string.pilih_tanggal_pulang))
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { openEndDatePicker() },
            trailingIcon = {
              Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar Icon",
                modifier = Modifier.clickable { openEndDatePicker() }
              )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Next
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
            value = selectedDestination,
            onValueChange = { /* Read-only field */ },
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
              },
            placeholder = {
              Text(stringResource(R.string.pilih_destinasi))
            },
            trailingIcon = {
              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Icon"
              )
            }
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Button(
            onClick = { /* Handle save action */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = stringResource(R.string.simpan),
              fontSize = 16.sp
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
//}
