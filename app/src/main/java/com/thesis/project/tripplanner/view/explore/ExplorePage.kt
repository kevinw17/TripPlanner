package com.thesis.project.tripplanner.view.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
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
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  authViewModel: AuthViewModel
) {

  val otherUserItinerary by itineraryViewModel.otherUserItinerary.collectAsState()

  LaunchedEffect(authViewModel.userId) {
    authViewModel.userId?.let { userId ->
      itineraryViewModel.loadOtherUsersItineraries(userId)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.explore),
            fontWeight = FontWeight.Bold,
            color = Color.Black
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
              modifier = Modifier.size(24.dp),
              tint = Color.Black
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
        .background(Color.White)
        .padding(paddingValues)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      item {
        Text(
          text = stringResource(R.string.explore_page_description),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 16.dp),
          color = Color.Black
        )
      }

      if (otherUserItinerary.isEmpty()) {
        item {
          Text(
            text = stringResource(R.string.no_itineraries_created),
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            textAlign = TextAlign.Center
          )
        }
      } else {
        items(otherUserItinerary.shuffled()) { otherUserItinerary ->
          ItineraryCard(
            username = otherUserItinerary.itinerary.username,
            profileImageUrl = otherUserItinerary.itinerary.profileImageUrl,
            title = otherUserItinerary.itinerary.title,
            description = otherUserItinerary.itinerary.description,
            onClick = { navController.navigate("detail_itinerary/${otherUserItinerary.itinerary.userId}/${otherUserItinerary.itinerary.itineraryId}") }
          )
        }
      }
    }
  }
}