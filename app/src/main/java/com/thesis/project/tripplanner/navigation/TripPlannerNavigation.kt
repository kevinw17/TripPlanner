package com.thesis.project.tripplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thesis.project.tripplanner.view.account.AccountPage
import com.thesis.project.tripplanner.view.account.ChangePasswordScreen
import com.thesis.project.tripplanner.view.account.EditProfileScreen
import com.thesis.project.tripplanner.view.account.FriendsPage
import com.thesis.project.tripplanner.view.account.FriendshipStatus
import com.thesis.project.tripplanner.view.account.ProfileScreen
import com.thesis.project.tripplanner.view.account.UserProfileScreen
import com.thesis.project.tripplanner.view.chat.ChatRoomScreen
import com.thesis.project.tripplanner.view.home.HomePage
import com.thesis.project.tripplanner.view.itinerary.ItineraryPage
import com.thesis.project.tripplanner.view.explore.ExplorePage
import com.thesis.project.tripplanner.view.explore.Itinerary
import com.thesis.project.tripplanner.view.itinerary.DetailItineraryScreen
import com.thesis.project.tripplanner.view.itinerary.ItineraryList
import com.thesis.project.tripplanner.view.login_register.LoginPage
import com.thesis.project.tripplanner.view.login_register.RegisterPage
import com.thesis.project.tripplanner.view.messages.MessagesScreen
import com.thesis.project.tripplanner.view.suggestion.SuggestionPage
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@Composable
fun TripPlannerNavigation(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  itineraryViewModel: ItineraryViewModel
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
          authViewModel = authViewModel,
          itineraryViewModel = itineraryViewModel
        )
      }
      composable("itinerary") {
        ItineraryPage(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel
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
          onSignOut = {
            authViewModel.signOut()
            navController.navigate("login")
          }
        )
      }
      composable("profile") {
        ProfileScreen(
          navController = navController,
          onChangePassword = { navController.navigate("change_password") },
          onEditProfile = { navController.navigate("edit_profile") },
          itineraryViewModel = itineraryViewModel
        )
      }
      composable("friends") {
        FriendsPage(
          navController = navController
        )
      }
      composable("edit_profile") {
        EditProfileScreen(
          navController = navController,
          onSaveChanges = { newName, newBio ->
            authViewModel.updateUserProfile(newName, newBio)
            navController.popBackStack()
          }
        )
      }
      composable("change_password") {
        ChangePasswordScreen(
          navController = navController,
          authViewModel = authViewModel
        )
      }
      composable("user_profile_screen") {
        UserProfileScreen(
          navController = navController,
          username = "Brian",
          bio = "I love travelling...",
          itinerariesCount = 3,
          friendsCount = 20,
          friendStatus = FriendshipStatus.FRIEND,
          itineraries = listOf(
            Itinerary("Brian", "Liburan ke Bandung", "Jalan-jalan ke Bandung sangat seru!!"),
            Itinerary("Brian", "Liburan ke Bandung", "Jalan-jalan ke Bandung sangat seru!!")
          ),
          onAddFriend = {
            // Implement friend request logic
          },
          onCancelRequest = {
            // Implement cancel request logic
          },
          onStartChat = {
            // Implement chat initiation logic
          }
        )
      }
      composable("itinerary_list") {
        ItineraryList(
          navController = navController
        )
      }
      composable("message") {
        MessagesScreen(
          navController = navController
        )
      }
      composable("detail_itinerary") {
        DetailItineraryScreen(
          navController = navController
        )
      }
      composable("chat_room") {
        ChatRoomScreen(
          navController = navController,
          onSendMessage = {}
        )
      }
    }
  )
}