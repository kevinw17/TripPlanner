package com.thesis.project.tripplanner.view.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thesis.project.tripplanner.R

@Composable
fun ItineraryCard(
  username: String,
  title: String,
  description: String,
  onClick: () -> Unit = {}
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() },
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
          painter = painterResource(R.drawable.ic_user_profile),
          contentDescription = "User Avatar",
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(text = username, fontWeight = FontWeight.Bold)
          Text(text = title)
          Text(text = description, color = Color.Gray)
        }
      }
    }
  }
}
