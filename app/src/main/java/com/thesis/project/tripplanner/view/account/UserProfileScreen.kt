package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  authViewModel: AuthViewModel,
  currentUserId: String,
  targetUserId: String,
) {

  val targetUsername by authViewModel.targetUsername.collectAsState()
  val bio by authViewModel.bio.collectAsState()
  val profileImageUrl by authViewModel.profileImageUrl.collectAsState()
  val itineraries by itineraryViewModel.itineraries.collectAsState()
  val itinerariesCount by itineraryViewModel.itineraryCount.collectAsState()
  val friends by itineraryViewModel.friends.collectAsState()
  val friendshipStatus by itineraryViewModel.friendshipStatus.collectAsState()
  var isCancelDialogVisible by remember { mutableStateOf(false) }

  LaunchedEffect(targetUserId) {
    if (targetUserId.isNotEmpty()) {
      itineraryViewModel.clearItineraries()
      authViewModel.loadUserProfileById(targetUserId)
      itineraryViewModel.loadItineraries(targetUserId)
      itineraryViewModel.checkFriendshipStatus(currentUserId, targetUserId)
    }
  }

  LaunchedEffect(friendshipStatus) {
    if (friendshipStatus == FriendshipStatus.FRIEND) {
      itineraryViewModel.loadFriends(currentUserId)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = targetUsername,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
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
    bottomBar = { BottomNavigationBar(navController) }
  ) { paddingValues ->
    UserProfileContent(
      navController = navController,
      username = targetUsername,
      profileImageUrl = profileImageUrl.toString(),
      bio = bio,
      itinerariesCount = itinerariesCount,
      friendsCount = friends.size,
      friendStatus = friendshipStatus,
      itineraries = itineraries,
      isCancelDialogVisible = isCancelDialogVisible,
      onAddFriend = { itineraryViewModel.sendFriendRequest(currentUserId, targetUserId) },
      onCancelRequest = { isCancelDialogVisible = true },
      onAcceptFriendRequest = { itineraryViewModel.acceptFriendRequest(currentUserId, targetUserId) },
      onStartChat = { navController.navigate("chat_room/$targetUserId") },
      onConfirmCancelRequest = {
        itineraryViewModel.cancelFriendRequest(currentUserId, targetUserId)
        isCancelDialogVisible = false
      },
      onDismissCancelDialog = { isCancelDialogVisible = false },
      paddingValues = paddingValues
    )
  }
}