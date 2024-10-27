package com.thesis.project.tripplanner.pages

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
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.components.DestinationBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryPage(
  navController: NavController
) {

  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  var selectedDestination by remember { mutableStateOf("") }

  val destinations = listOf("Jakarta", "Bandung", "Bali", "Surabaya", "Yogyakarta", "Medan", "Makassar")

  BottomSheetScaffold(
    scaffoldState = scaffoldState,
    sheetContent = {
      DestinationBottomSheet(
        destinations = destinations,
        onSelect = { destination ->
          selectedDestination = destination
          coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
        }
      )
    }
  ) {
    Scaffold(topBar = {
      TopAppBar(title = { Text("Itinerary", fontSize = 20.sp) }, navigationIcon = {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
      }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }, bottomBar = {
      BottomNavigationBar(navController)
    }) { paddingValues ->
      LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        verticalArrangement = Arrangement.Top
      ) {
        item {
          Text(text = "Buat Itinerary", fontSize = 20.sp)
          Spacer(modifier = Modifier.height(32.dp))
        }

        item {
          Text(text = "Judul Trip", color = Color.Black)
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = "",
            onValueChange = { /* Handle title input */ },
            placeholder = { Text("Masukkan judul") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Text(text = "Deskripsi", color = Color.Black)
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = "",
            onValueChange = { /* Handle description input */ },
            placeholder = { Text("Masukkan deskripsi") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Text(text = "Tanggal Pergi", color = Color.Black)
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = "",
            onValueChange = { /* Handle start date input */ },
            placeholder = { Text("Pilih tanggal pergi") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
              Icon(Icons.Default.DateRange, contentDescription = "Calendar Icon")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            )
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Text(text = "Tanggal Pulang", color = Color.Black)
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
            value = "",
            onValueChange = { /* Handle end date input */ },
            placeholder = { Text("Pilih tanggal pulang") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
              Icon(Icons.Default.DateRange, contentDescription = "Calendar Icon")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            )
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Text("Tujuan", color = Color.Black)
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = selectedDestination,
            onValueChange = { /* Read-only field */ },
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
              },
            placeholder = { Text("Pilih destinasi") },
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
            },
            readOnly = true
          )
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Button(
            onClick = { /* Handle save action */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Simpan", fontSize = 16.sp)
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}
