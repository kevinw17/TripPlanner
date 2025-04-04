package com.thesis.project.tripplanner.view.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard
import com.thesis.project.tripplanner.viewmodel.AuthState
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class) @Composable
fun HomePage(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel,
  itineraryViewModel: ItineraryViewModel
) {

  val otherUserItinerary by itineraryViewModel.otherUserItinerary.collectAsState()
  val authState = authViewModel.authState.observeAsState()
  val username by authViewModel.username.collectAsState()
  val destinations by itineraryViewModel.destinations.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(authViewModel.userId) {
    authViewModel.userId?.let { userId ->
      itineraryViewModel.loadOtherUsersItineraries(userId)
    }
  }

  LaunchedEffect(Unit) {
    itineraryViewModel.loadDestinations()
  }

  LaunchedEffect(authState.value) {
    when (authState.value) {
      is AuthState.Authenticated -> {
        if (authViewModel.isNewUser) {
          Toast.makeText(context,
            context.getString(R.string.registrasi_berhasil), Toast.LENGTH_SHORT).show()
          authViewModel.isNewUser = false
        }
      }
      is AuthState.Unauthenticated -> {
        navController.navigate("login")
      }
      else -> Unit
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.trip_planner_app),
            fontWeight = FontWeight.Bold
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White,
          titleContentColor = Color.Black
        )
      )
    },
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      item {
        Text(
          text = stringResource(R.string.welcome_user, username),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      item {
        GoToItineraryCard(navController)
        Spacer(modifier = Modifier.height(16.dp))
      }

      item {
        Text(
          text = stringResource(R.string.itinerary_wisatawan_lain),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      if (otherUserItinerary.isEmpty()) {
        item {
          androidx.compose.material.Text(
            text = stringResource(R.string.no_itineraries_created),
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            textAlign = TextAlign.Center
          )
        }
      } else {
        items(otherUserItinerary.shuffled().take(3)) { otherUserItinerary ->
          ItineraryCard(
            username = otherUserItinerary.username,
            profileImageUrl = otherUserItinerary.profileImageUrl,
            title = otherUserItinerary.itinerary.title,
            description = otherUserItinerary.itinerary.description,
            onClick = { navController.navigate("detail_itinerary/${otherUserItinerary.itinerary.userId}/${otherUserItinerary.itinerary.itineraryId}") }
          )

          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = stringResource(R.string.lihat_lebih_banyak),
              fontSize = 14.sp,
              color = Color.Blue,
              modifier = Modifier.clickable { navController.navigate("explore") },
              textAlign = TextAlign.Center
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = stringResource(R.string.suggestions),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      items(destinations.take(3)) { destination ->
        Card(
          shape = RoundedCornerShape(8.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp)
            .clickable {
              navController.navigate("suggestion/${destination.name}")
            },
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
          border = BorderStroke(
            width = 1.dp,
            color = Color.Black
          )
        ) {
          Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(destination.name, fontSize = 16.sp, color = Color.Black)
          }
        }
      }

      item {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Lihat lebih banyak",
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier.clickable { navController.navigate("suggestion") },
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}