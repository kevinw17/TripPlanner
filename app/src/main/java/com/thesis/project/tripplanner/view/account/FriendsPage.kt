package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.data.Friend
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsPage(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  currentUserId: String
) {
  val friends by itineraryViewModel.friends.collectAsState()

  LaunchedEffect(Unit) {
    itineraryViewModel.loadFriends(currentUserId)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.friends),
            fontWeight = FontWeight.Bold,
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
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(paddingValues)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = "${friends.size} Friends",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
      )

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(friends) { friend ->
          FriendsList(friend = friend) { friendId ->
            navController.navigate("user_profile_screen/$friendId")
          }
        }
      }
    }
  }
}

@Composable
fun FriendsList(
  friend: Friend,
  onFriendClick: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .clickable { onFriendClick(friend.id) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(id = R.drawable.ic_user_profile),
      contentDescription = "Friend Icon",
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(text = friend.name, fontSize = 16.sp)
  }
}