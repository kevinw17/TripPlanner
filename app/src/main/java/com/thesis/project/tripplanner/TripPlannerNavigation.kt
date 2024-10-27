package com.thesis.project.tripplanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thesis.project.tripplanner.pages.HomePage
import com.thesis.project.tripplanner.pages.ItineraryPage
import com.thesis.project.tripplanner.pages.LoginPage
import com.thesis.project.tripplanner.pages.RegisterPage
import com.thesis.project.tripplanner.viewmodel.AuthViewModel

@Composable
fun TripPlannerNavigation(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel
) {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "login",
    builder = {
      composable("login") {
        LoginPage(
          modifier = modifier,
          navController = navController,
          authViewModel = authViewModel
        )
      }
      composable("register") {
        RegisterPage(
          modifier = modifier,
          navController = navController,
          authViewModel = authViewModel
        )
      }
      composable("home") {
        HomePage(
          modifier = modifier,
          navController = navController,
          authViewModel = authViewModel
        )
      }
      composable("itinerary") {
        ItineraryPage(
          navController = navController
        )
      }
    }
  )
}