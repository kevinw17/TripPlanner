package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.SaveChangesDialog
import com.thesis.project.tripplanner.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  navController: NavController,
  authViewModel: AuthViewModel,
  onSaveChanges: (String) -> Unit
) {
  val username by authViewModel.username.collectAsState()
  val bio by authViewModel.bio.collectAsState()

  val bioState = remember { mutableStateOf(TextFieldValue(bio)) }
  val showDialog = remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.edit_profile),
            fontSize = 20.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = {
              navController.popBackStack()
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_arrow_left),
              contentDescription = "Back",
              modifier = Modifier.size(24.dp),
              tint = Color.Black
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(paddingValues)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = stringResource(R.string.name),
        color = Color.Black
      )

      OutlinedTextField(
        value = username,
        onValueChange = {},
        placeholder = {
          Text(stringResource(R.string.name))
        },
        enabled = false,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          disabledBorderColor = Color.Black,
          disabledPlaceholderColor = Color.DarkGray,
          disabledTextColor = Color.DarkGray
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(R.string.bio),
        color = Color.Black
      )

      OutlinedTextField(
        value = bioState.value,
        onValueChange = { bioState.value = it },
        placeholder = {
          Text(stringResource(R.string.bio))
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedPlaceholderColor = Color.Black,
          unfocusedPlaceholderColor = Color.Black
        )
      )

      Spacer(modifier = Modifier.weight(1f))

      Button(
        onClick = { showDialog.value = true },
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
          containerColor = Color(0xFFDFF9FF),
          contentColor = Color.Black,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = stringResource(R.string.save_changes),
          fontSize = 16.sp
        )
      }
    }
  }

  if (showDialog.value) {
    SaveChangesDialog(
      onConfirm = {
        onSaveChanges(bioState.value.text)
        showDialog.value = false
      },
      onDismiss = {
        showDialog.value = false
      }
    )
  }
}