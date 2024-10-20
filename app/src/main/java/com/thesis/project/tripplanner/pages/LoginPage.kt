package com.thesis.project.tripplanner.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.Utils
import com.thesis.project.tripplanner.viewmodel.AuthState
import com.thesis.project.tripplanner.viewmodel.AuthViewModel

@Composable
fun LoginPage(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel
) {

  var email by remember { mutableStateOf(Utils.EMPTY) }
  var password by remember { mutableStateOf(Utils.EMPTY) }
  val authState = authViewModel.authState.observeAsState()
  val context = LocalContext.current
  val auth = FirebaseAuth.getInstance()

  LaunchedEffect(authState.value) {
    when(authState.value) {
      is AuthState.Authenticated -> navController.navigate("home")
      is AuthState.Error -> Toast.makeText(
        context,
        (authState.value as AuthState.Error).message,
        Toast.LENGTH_SHORT
      ).show()
      else -> Unit
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFDFF9FF))
  ) {
    Column(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.login_to_trip_planner_app),
        fontSize = 24.sp
      )

      Spacer(modifier = Modifier.height(64.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 54.dp)
      ) {
        Text(
          text = stringResource(R.string.email),
          color = Color.Black
        )

        OutlinedTextField(
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
          value = email,
          onValueChange = {
            email = it
          },
          label = {
            Text(text = stringResource(R.string.masukkan_email))
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 54.dp)
      ) {
        Text(
          text = stringResource(R.string.password),
          color = Color.Black
        )

        PasswordField(
          password = password,
          onPasswordChange = { password = it },
          isError = false
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = { authViewModel.login(email, password) },
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        )
      ) {
        Text(text = stringResource(R.string.login))
      }

      Spacer(modifier = Modifier.height(8.dp))

      TextButton(
        onClick = {
          navController.navigate("register")
        }
      ) {
        Text(
          text = stringResource(R.string.not_registered_hyperlink),
          color = Color(0xFF005FED)
        )
      }
    }
  }
}