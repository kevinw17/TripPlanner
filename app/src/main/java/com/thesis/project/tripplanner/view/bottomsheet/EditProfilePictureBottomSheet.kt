package com.thesis.project.tripplanner.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesis.project.tripplanner.R

@Composable
fun EditProfilePictureBottomSheet(
  onNewProfilePicture: () -> Unit,
  onRemoveProfilePicture: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    // New profile picture option
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNewProfilePicture() }
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_image_placeholder),
        contentDescription = "New profile picture",
        tint = Color.Black,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "New profile picture",
        fontSize = 16.sp,
        color = Color.Black
      )
    }
    Divider()

    // Remove profile picture option
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onRemoveProfilePicture() }
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_remove),
        contentDescription = "Remove profile picture",
        tint = Color.Red,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Remove profile picture",
        fontSize = 16.sp,
        color = Color.Red
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePictureBottomSheetPreview() {
  EditProfilePictureBottomSheet(
    onNewProfilePicture = { /* Handle new profile picture */ },
    onRemoveProfilePicture = { /* Handle remove profile picture */ }
  )
}
