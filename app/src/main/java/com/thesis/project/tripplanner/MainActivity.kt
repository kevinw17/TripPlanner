package com.thesis.project.tripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.FirebaseApp
import com.thesis.project.tripplanner.navigation.TripPlannerNavigation
import com.thesis.project.tripplanner.ui.theme.TripPlannerTheme
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ChatViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel
import com.thesis.project.tripplanner.viewmodel.SplashScreenViewModel

class MainActivity : ComponentActivity() {

  private val splashScreenViewModel: SplashScreenViewModel by viewModels()
  private val authViewModel: AuthViewModel by viewModels()
  private val itineraryViewModel: ItineraryViewModel by viewModels()
  private val chatViewModel: ChatViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FirebaseApp.initializeApp(this)
    installSplashScreen().apply {
      setKeepOnScreenCondition {
        splashScreenViewModel.isLoading.value
      }
    }
    setContent {
      TripPlannerTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          TripPlannerNavigation(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel,
            itineraryViewModel = itineraryViewModel,
            chatViewModel = chatViewModel
          )
        }
      }
    }
  }
}