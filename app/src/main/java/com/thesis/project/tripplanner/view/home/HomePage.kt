package com.thesis.project.tripplanner.view.home

import android.widget.Toast
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

@OptIn(ExperimentalMaterial3Api::class) @Composable
fun HomePage(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel
) {

  val authState = authViewModel.authState.observeAsState()
  val context = LocalContext.current

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
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      item {
        Text(text = "Selamat datang, [username]", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
      }

      item {
        GoToItineraryCard(navController)
        Spacer(modifier = Modifier.height(16.dp))
      }

      item {
        Text(text = stringResource(R.string.itinerary_wisatawan_lain), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
      }

      items(2) {
        ItineraryCard(
          username = "andy123",
          title = "Liburan ke Bali",
          description = "Jalan-jalan ke Bali sangat seru!",
          onClick = { /* Handle click */ }
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
            modifier = Modifier.clickable { /* Handle click action */ },
            textAlign = TextAlign.Center
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.suggestions), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
      }

      items(listOf("Jakarta", "Bandung", "Bali")) { place ->
        Card(
          shape = RoundedCornerShape(8.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp)
            .clickable { },
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
        ) {
          Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(place, fontSize = 16.sp, color = Color.Black)
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
            modifier = Modifier.clickable { /* Handle click action */ },
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}