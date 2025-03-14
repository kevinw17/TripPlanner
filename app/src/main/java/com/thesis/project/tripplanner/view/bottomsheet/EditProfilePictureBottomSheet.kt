package com.thesis.project.tripplanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
      .background(Color.White)
      .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
      .heightIn(max = 250.dp)
  ) {
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
        text = stringResource(R.string.foto_profil_baru),
        fontSize = 16.sp,
        color = Color.Black
      )
    }
    Divider()

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onRemoveProfilePicture() }
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_remove),
        contentDescription = stringResource(R.string.hapus_foto_profil),
        tint = Color.Red,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = stringResource(R.string.hapus_foto_profil),
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
    onNewProfilePicture = {},
    onRemoveProfilePicture = {}
  )
}