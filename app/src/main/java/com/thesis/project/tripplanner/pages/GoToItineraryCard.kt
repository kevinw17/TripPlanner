package com.thesis.project.tripplanner.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R

@Composable
fun GoToItineraryCard(navController: NavController) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
    shape = RoundedCornerShape(8.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(stringResource(R.string.itinerary_card_description), fontSize = 16.sp)
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { navController.navigate("itinerary") },
      ) {
        Text(stringResource(R.string.buat_itinerary))
      }
    }
  }
}