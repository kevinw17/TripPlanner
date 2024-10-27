package com.thesis.project.tripplanner.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DestinationBottomSheet(
  destinations: List<String>,
  onSelect: (String) -> Unit
) {
  Column(modifier = Modifier.padding(16.dp)) {
    Text(text = "Select destinations", fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn {
      items(destinations) { destination ->
        Text(
          text = destination,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(destination) }
            .padding(vertical = 12.dp)
        )
        Divider()
      }
    }
  }
}
