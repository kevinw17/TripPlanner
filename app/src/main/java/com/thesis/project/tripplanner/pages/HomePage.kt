package com.thesis.project.tripplanner.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.viewmodel.AuthState
import com.thesis.project.tripplanner.viewmodel.AuthViewModel

@Composable
fun HomePage(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel
) {

  val authState = authViewModel.authState.observeAsState()
  val context = LocalContext.current

  LaunchedEffect(authState.value) {
    when (authState.value) {
      is AuthState.Authenticated -> {
        if (authViewModel.isNewUser) {
          Toast.makeText(context,
            context.getString(R.string.registrasi_berhasil), Toast.LENGTH_SHORT).show()
          authViewModel.isNewUser = false
        }
      }
      is AuthState.Unauthenticated -> {
        navController.navigate("login")
      }
      else -> Unit
    }
  }
  
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) { 
    
    Text(text = "Halaman Utama", fontSize = 32.sp)

    Spacer(modifier = Modifier.height(16.dp))
    
    Button(
      onClick = { authViewModel.signOut() }
    ) {
      Text(text = stringResource(R.string.keluar))
    }
  }
}