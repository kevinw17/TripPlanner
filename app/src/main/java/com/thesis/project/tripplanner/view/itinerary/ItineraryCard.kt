package com.thesis.project.tripplanner.view.itinerary

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.rememberAsyncImagePainter
import com.thesis.project.tripplanner.R

@Composable
fun ItineraryCard(
  username: String,
  profileImageUrl : String?,
  title: String,
  description: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() },
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
    border = BorderStroke(
      width = 1.dp,
      color = Color.Black
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
          painter = rememberAsyncImagePainter(
            model = profileImageUrl ?: R.drawable.ic_user_profile,
            placeholder = painterResource(R.drawable.ic_user_profile),
            error = painterResource(R.drawable.ic_user_profile)
          ),
          contentDescription = "User Avatar",
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = username, fontWeight = FontWeight.Bold)
      }
      Spacer(modifier = Modifier.height(2.dp))
      Column {
        Text(text = title)
        Text(
          text = description,
          color = Color.Gray,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
