package com.thesis.project.tripplanner.view.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryList(
  navController: NavController,
  itineraries: List<Itinerary> = listOf(
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!")
  )
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.list_itinerary),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Text(
          text = "${itineraries.size} ${stringResource(R.string.itineraries)}",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }

      items(itineraries) { itinerary ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Handle click on itinerary */ },
          shape = RoundedCornerShape(8.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
        ) {
          Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(R.drawable.ic_user_profile),
              contentDescription = "User Avatar",
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(text = itinerary.username, fontWeight = FontWeight.Bold)
              Text(text = itinerary.title)
              Text(text = itinerary.description, color = Color.Gray, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
