package com.thesis.project.tripplanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesis.project.tripplanner.R

@Composable
fun DestinationBottomSheet(
  destinations: List<String>,
  onSelect: (String) -> Unit
) {
  Column(modifier = Modifier
    .fillMaxWidth()
    .background(Color.White)
  ) {
    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = stringResource(R.string.pilih_destinasi),
      fontSize = 18.sp,
      color = Color.Black
    )
    Spacer(modifier = Modifier.height(8.dp))
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
      .heightIn(max = 400.dp)
  ) {
    items(destinations) { destination ->
      Text(
        text = destination,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSelect(destination) }
          .padding(vertical = 12.dp),
        color = Color.Black
      )
      Divider(color = Color.Black)
    }
  }
}