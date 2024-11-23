package com.thesis.project.tripplanner.view.suggestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SuggestionPage(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  authViewModel: AuthViewModel,
  selectedDestinationName: String? = null
) {
  val selectedDestination = remember { mutableStateOf(Utils.EMPTY) }
  val destinations by itineraryViewModel.destinations.collectAsState()
  val selectedDestinationDescription = destinations.find { it.name == selectedDestination.value }?.description
  val filteredItineraries by itineraryViewModel.filteredItineraries.collectAsState()
  val currentUserId = authViewModel.userId

  LaunchedEffect(Unit) {
    itineraryViewModel.loadDestinations()
  }

  LaunchedEffect(destinations) {
    if (destinations.isNotEmpty()) {
      selectedDestination.value = selectedDestinationName ?: destinations[0].name
      selectedDestination.value.let { destinationName ->
        currentUserId?.let { userId ->
          itineraryViewModel.loadItinerariesByDestination(destinationName, userId)
        }
      }
    }
  }

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
        LazyRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(destinations) { destination ->
            Chip(
              onClick = {
                selectedDestination.value = destination.name
                currentUserId?.let {
                  itineraryViewModel.loadItinerariesByDestination(destination.name, it)
                }
              },
              colors = if (selectedDestination.value == destination.name) {
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
              Text(destination.name)
            }
          }
        }
      }

      item {
        val cityImageRes = when (selectedDestination.value) {
          stringResource(R.string.jakarta) -> R.drawable.jakarta
          stringResource(R.string.bandung) -> R.drawable.bandung
          stringResource(R.string.bali) -> R.drawable.bali
          stringResource(R.string.surabaya) -> R.drawable.surabaya
          stringResource(R.string.yogyakarta) -> R.drawable.yogyakarta
          else -> R.drawable.wisata
        }
        Image(
          painter = painterResource(cityImageRes),
          contentDescription = selectedDestination.value,
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(vertical = 2.dp)
        )
      }

      item {
        Text(text = selectedDestination.value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
          text = selectedDestinationDescription.orEmpty(),
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      if (selectedDestination.value.isNotEmpty()) {
        item {
          Text(
            text = "Rekomendasi Itinerary untuk ${selectedDestination.value}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }

        if (filteredItineraries.isEmpty()) {
          item {
            Text(
              text = "Tidak ada itinerary untuk tujuan ${selectedDestination.value} saat ini",
              fontSize = 16.sp,
              color = Color.DarkGray,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
              textAlign = TextAlign.Center
            )
          }
        } else {
          items(filteredItineraries) { itinerary ->
            ItineraryCard(
              username = itinerary.username,
              title = itinerary.title,
              description = itinerary.description,
              profileImageUrl = itinerary.profileImageUrl,
              onClick = {
                navController.navigate("detail_itinerary/${itinerary.userId}/${itinerary.itineraryId}")
              }
            )
          }
        }
      }
    }
  }
}