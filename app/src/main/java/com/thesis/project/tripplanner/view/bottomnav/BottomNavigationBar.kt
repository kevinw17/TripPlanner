package com.thesis.project.tripplanner.view.bottomnav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R

@Composable
fun BottomNavigationBar(navController: NavController) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
  ) {
    BottomNavigationItem(
      icon = R.drawable.ic_home,
      label = "Home",
      onClick = { navController.navigate("home") }
    )
    BottomNavigationItem(
      icon = R.drawable.ic_itinerary,
      label = "Itinerary",
      onClick = { navController.navigate("itinerary") }
    )
    BottomNavigationItem(
      icon = R.drawable.ic_explore,
      label = "Explore",
      onClick = { navController.navigate("explore") }
    )
    BottomNavigationItem(
      icon = R.drawable.ic_suggestion,
      label = "Suggestions",
      onClick = { /* Navigate to Suggestion */ }
    )
    BottomNavigationItem(
      icon = R.drawable.ic_account,
      label = "Account",
      onClick = { /* Navigate to Account */ }
    )
  }
}