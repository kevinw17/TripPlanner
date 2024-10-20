package com.thesis.project.tripplanner

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.thesis.project.tripplanner.presentation.profile.ProfileScreen
import com.thesis.project.tripplanner.presentation.sign_in.GoogleAuthUiClient
import com.thesis.project.tripplanner.presentation.sign_in.SignInScreen
import com.thesis.project.tripplanner.presentation.sign_in.SignInViewModel
import com.thesis.project.tripplanner.ui.theme.TripPlannerTheme
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val splashScreenViewModel: SplashScreenViewModel by viewModels()
  private val authViewModel: AuthViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen().apply {
      setKeepOnScreenCondition {
        splashScreenViewModel.isLoading.value
      }
    }
    enableEdgeToEdge()
    setContent {
      TripPlannerTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          TripPlannerNavigation(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel
          )
        }
      }
    }
  }
}
