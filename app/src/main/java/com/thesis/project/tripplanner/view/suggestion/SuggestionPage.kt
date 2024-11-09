package com.thesis.project.tripplanner.view.suggestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.explore.Itinerary
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SuggestionPage(
  navController: NavController
) {
  val cities = listOf("Jakarta", "Bandung", "Bali")
  val selectedCity = remember { mutableStateOf(cities[0]) }
  val cityDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
  val itineraries = listOf<Itinerary>(
    Itinerary("bud456", "Liburan ke ${selectedCity.value}", "Jalan-jalan ke ${selectedCity.value} sangat seru! apalagi kalau bareng keluarga"),
    Itinerary("bud456", "Liburan ke ${selectedCity.value}", "Jalan-jalan ke ${selectedCity.value} sangat seru! apalagi kalau bareng keluarga"),
    Itinerary("bud456", "Liburan ke ${selectedCity.value}", "Jalan-jalan ke ${selectedCity.value} sangat seru! apalagi kalau bareng keluarga"),
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.suggestion),
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              painter = painterResource(R.drawable.ic_arrow_left),
              contentDescription = "Back",
              modifier = Modifier.size(24.dp)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        ),
      )
    },
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          cities.forEach { city ->
            Chip(
              onClick = { selectedCity.value = city },
              colors = if (selectedCity.value == city) {
                ChipDefaults.chipColors(
                  backgroundColor = Color(0xFFDFF9FF),
                  contentColor = Color.Black
                )
              } else {
                ChipDefaults.chipColors(
                  backgroundColor = Color(0xFFE0E0E0),
                  contentColor = Color.Black
                )
              }
            ) {
              Text(city)
            }
          }
        }
      }

      item {
        val cityImageRes = when (selectedCity.value) {
          "Jakarta" -> R.drawable.jakarta
          "Bandung" -> R.drawable.bandung
          "Bali" -> R.drawable.bali
          else -> R.drawable.jakarta
        }
        Image(
          painter = painterResource(cityImageRes),
          contentDescription = selectedCity.value,
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(vertical = 2.dp)
        )
      }

      item {
        Text(text = selectedCity.value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
          text = cityDescription,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      item {
        Text(
          text = "Rekomendasi Itinerary ${selectedCity.value}",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }

      items(itineraries) { itinerary ->
        ItineraryCard(
          username = itinerary.username,
          title = itinerary.title,
          description = itinerary.description,
          onClick = { navController.navigate("detail_itinerary") }
        )
      }
    }
  }
}