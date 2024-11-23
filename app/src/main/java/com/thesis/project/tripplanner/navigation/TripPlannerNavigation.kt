package com.thesis.project.tripplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.account.AccountPage
import com.thesis.project.tripplanner.view.account.ChangePasswordScreen
import com.thesis.project.tripplanner.view.account.EditProfileScreen
import com.thesis.project.tripplanner.view.account.FriendsPage
import com.thesis.project.tripplanner.view.account.ProfileScreen
import com.thesis.project.tripplanner.view.account.UserProfileScreen
import com.thesis.project.tripplanner.view.chat.ChatRoomScreen
import com.thesis.project.tripplanner.view.explore.ExplorePage
import com.thesis.project.tripplanner.view.home.HomePage
import com.thesis.project.tripplanner.view.itinerary.DetailItineraryScreen
import com.thesis.project.tripplanner.view.itinerary.ItineraryList
import com.thesis.project.tripplanner.view.itinerary.ItineraryPage
import com.thesis.project.tripplanner.view.login_register.LoginPage
import com.thesis.project.tripplanner.view.login_register.RegisterPage
import com.thesis.project.tripplanner.view.messages.MessagesScreen
import com.thesis.project.tripplanner.view.suggestion.SuggestionPage
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ChatViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@Composable
fun TripPlannerNavigation(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  itineraryViewModel: ItineraryViewModel,
  chatViewModel: ChatViewModel
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
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel
        )
      }
      composable("suggestion") {
        SuggestionPage(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel
        )
      }
      composable("suggestion/{destinationName}") { backStackEntry ->
        val destinationName = backStackEntry.arguments?.getString("destinationName") ?: Utils.EMPTY
        SuggestionPage(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel,
          selectedDestinationName = destinationName
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
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel
        )
      }
      composable("friends") {
        FriendsPage(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          currentUserId = authViewModel.userId ?: Utils.EMPTY
        )
      }
      composable("edit_profile") {
        EditProfileScreen(
          navController = navController,
          onSaveChanges = { newBio ->
            authViewModel.updateUserProfile(newBio)
            navController.popBackStack()
          },
          authViewModel = authViewModel
        )
      }
      composable("change_password") {
        ChangePasswordScreen(
          navController = navController,
          authViewModel = authViewModel
        )
      }
      composable("user_profile_screen/{userId}") { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: Utils.EMPTY
        UserProfileScreen(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel,
          currentUserId = authViewModel.userId ?: Utils.EMPTY,
          targetUserId = userId
        )
      }
      composable("itinerary_list") {
        ItineraryList(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel
        )
      }
      composable("message") {
        MessagesScreen(
          navController = navController,
          chatViewModel = chatViewModel,
          currentUserId = authViewModel.userId ?: Utils.EMPTY
        )
      }
      composable(
        route = "detail_itinerary/{userId}/{itineraryId}",
        arguments = listOf(
          navArgument("userId") { type = NavType.StringType },
          navArgument("itineraryId") { type = NavType.StringType }
        )
      ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: Utils.EMPTY
        val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: Utils.EMPTY

        DetailItineraryScreen(
          navController = navController,
          itineraryViewModel = itineraryViewModel,
          authViewModel = authViewModel,
          itineraryId = itineraryId,
          currentUserId = userId
        )
      }
      composable(
        route = "chat_room/{userId}",
        arguments = listOf(
          navArgument("userId") { type = NavType.StringType }
        )
      ) { backStackEntry ->
        val otherUserId = backStackEntry.arguments?.getString("userId") ?: Utils.EMPTY
        ChatRoomScreen(
          navController = navController,
          chatViewModel = chatViewModel,
          currentUserId = authViewModel.userId.orEmpty(),
          otherUserId = otherUserId
        )
      }
    }
  )
}