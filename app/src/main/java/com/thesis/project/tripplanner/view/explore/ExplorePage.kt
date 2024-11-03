package com.thesis.project.tripplanner.view.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(
  navController: NavController,
  itineraries: List<Itinerary> = listOf(
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!"),
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
            text = stringResource(R.string.explore),
            fontWeight = FontWeight.Bold
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
        .padding(paddingValues),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp)
    ) {
      item {
        Text(
          text = stringResource(R.string.explore_page_description),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 16.dp)
        )
      }

      items(itineraries.size) { index ->
        val itinerary = itineraries[index]
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

data class Itinerary(
  val username: String,
  val title: String,
  val description: String
)

