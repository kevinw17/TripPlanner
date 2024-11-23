package com.thesis.project.tripplanner.view.account

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.SaveChangesDialog
import com.thesis.project.tripplanner.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
  navController: NavController,
  authViewModel: AuthViewModel
) {
  val oldPasswordState = remember { mutableStateOf(TextFieldValue()) }
  val newPasswordState = remember { mutableStateOf(TextFieldValue()) }
  val confirmPasswordState = remember { mutableStateOf(TextFieldValue()) }
  var showDialog by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val auth = FirebaseAuth.getInstance()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.change_password),
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
              modifier = Modifier.size(24.dp)
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
        .padding(paddingValues)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text(
        text = stringResource(R.string.old_password),
        color = Color.Black
      )

      OutlinedTextField(
        value = oldPasswordState.value,
        onValueChange = { oldPasswordState.value = it },
        placeholder = {
          Text(
            text = stringResource(R.string.old_password)
          )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedLabelColor = Color.Black,
          unfocusedLabelColor = Color.Black
        )
      )

      Text(
        text = stringResource(R.string.new_password),
        color = Color.Black
      )

      OutlinedTextField(
        value = newPasswordState.value,
        onValueChange = { newPasswordState.value = it },
        placeholder = {
          Text(
            text = stringResource(R.string.new_password)
          )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedLabelColor = Color.Black,
          unfocusedLabelColor = Color.Black
        )
      )

      Text(
        text = stringResource(R.string.confirmation_password),
        color = Color.Black
      )

      OutlinedTextField(
        value = confirmPasswordState.value,
        onValueChange = { confirmPasswordState.value = it },
        placeholder = {
          Text(
            text = stringResource(R.string.confirmation_password)
          )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedLabelColor = Color.Black,
          unfocusedLabelColor = Color.Black
        )
      )

      errorMessage?.let {
        Text(
          text = it,
          color = Color.Red,
          modifier = Modifier.padding(top = 8.dp)
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      Button(
        onClick = {
          if (newPasswordState.value.text != confirmPasswordState.value.text) {
            errorMessage = context.getString(R.string.password_not_match)
          } else {
            showDialog = true
          }
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFDFF9FF),
          contentColor = Color.Black
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

      if (showDialog) {
        SaveChangesDialog(
          onConfirm = {
            showDialog = false
            authViewModel.changePassword(
              auth = auth,
              oldPassword = oldPasswordState.value.text,
              newPassword = newPasswordState.value.text,
              onSuccess = {
                errorMessage = null
                Toast.makeText(
                  context,
                  context.getString(R.string.password_successfully_changed),
                  Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
              },
              onError = { error -> errorMessage = error }
            )
          },
          onDismiss = { showDialog = false }
        )
      }
    }
  }
}