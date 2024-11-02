package com.thesis.project.tripplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thesis.project.tripplanner.view.account.AccountPage
import com.thesis.project.tripplanner.view.account.FriendsPage
import com.thesis.project.tripplanner.view.account.ProfileScreen
import com.thesis.project.tripplanner.view.home.HomePage
import com.thesis.project.tripplanner.view.itinerary.ItineraryPage
import com.thesis.project.tripplanner.view.explore.ExplorePage
import com.thesis.project.tripplanner.view.login_register.LoginPage
import com.thesis.project.tripplanner.view.login_register.RegisterPage
import com.thesis.project.tripplanner.view.suggestion.SuggestionPage
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
      composable("explore") {
        ExplorePage(
          navController = navController
        )
      }
      composable("suggestion") {
        SuggestionPage(
          navController = navController
        )
      }
      composable("account") {
        AccountPage(
          navController = navController,
          onSignOut = { authViewModel.signOut() }
        )
      }
      composable("profile") {
        ProfileScreen(
          navController = navController,
          onChangePassword = {},
          onEditProfile = {}
        )
      }
      composable("friends") {
        FriendsPage(
          navController = navController
        )
      }
    }
  )
}