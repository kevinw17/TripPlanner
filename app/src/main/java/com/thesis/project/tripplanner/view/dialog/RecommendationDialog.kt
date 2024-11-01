package com.thesis.project.tripplanner.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thesis.project.tripplanner.R

@Composable
fun RecommendationDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = { onDismiss() },
    title = {
      Text(
        text = stringResource(R.string.konfirmasi_rekomendasi),
        style = MaterialTheme.typography.h6
      )
    },
    text = {
      Text(
        text = stringResource(R.string.recommendation_dialog_description)
      )
    },
    confirmButton = {
      Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Button(
          onClick = { onDismiss() },
          modifier = Modifier.weight(1f).padding(start = 8.dp, end = 4.dp),
          colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
            contentColor = Color.Black,
          ),
          border = BorderStroke(
            width = 1.dp,
            color = Color.Black
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("No")
        }
        Button(
          onClick = { onConfirm() },
          modifier = Modifier.weight(1f).padding(start = 4.dp, end = 8.dp),
          colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFDFF9FF),
            contentColor = Color.Black
          ),
          border = BorderStroke(
            width = 1.dp,
            color = Color.Black
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Yes")
        }
      }
    }
  )
}

@Preview
@Composable
fun RecommendationDialogPreview() {
  RecommendationDialog(
    onConfirm = {},
    onDismiss = {}
  )
}