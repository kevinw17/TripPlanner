package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.CancelFriendRequestDialog
import com.thesis.project.tripplanner.view.explore.Itinerary
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
  navController: NavController,
  username: String,
  bio: String,
  itinerariesCount: Int,
  friendsCount: Int,
  friendStatus: FriendshipStatus,
  itineraries: List<Itinerary>,
  onAddFriend: () -> Unit,
  onCancelRequest: () -> Unit,
  onStartChat: () -> Unit
) {
  var isCancelDialogVisible by remember { mutableStateOf(false) }
  var friendshipStatus by remember { mutableStateOf(friendStatus) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = username,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back"
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Row{
          Column(
            modifier = Modifier.padding(16.dp)
          ) {
            Box(
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
            ) {
              Image(
                painter = painterResource(R.drawable.ic_user_profile),
                contentDescription = "User Avatar",
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = username,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              modifier = Modifier.padding(horizontal = 8.dp)
            )
          }

          Box(
            modifier = Modifier
              .padding(16.dp)
              .align(Alignment.CenterVertically)
          ) {
            Row(
              horizontalArrangement = Arrangement.SpaceEvenly,
              modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$itinerariesCount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Itineraries", fontSize = 18.sp, color = Color.Gray)
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$friendsCount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Friends", fontSize = 18.sp, color = Color.Gray)
              }
            }
          }
        }
      }

      item {
        Text(
          text = bio,
          fontSize = 14.sp,
          color = Color.Gray,
          modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
        )
      }

      item {
        when (friendshipStatus) {
          FriendshipStatus.NOT_FRIEND -> {
            Button(
              onClick = {
                friendshipStatus = FriendshipStatus.REQUEST_SENT
                onAddFriend()
              },
              shape = RoundedCornerShape(24.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDFF9FF),
                contentColor = Color.Black
              ),
              border = BorderStroke(1.dp, Color.Black),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(stringResource(R.string.add_friend))
            }
          }
          FriendshipStatus.REQUEST_SENT -> {
            Button(
              onClick = { isCancelDialogVisible = true },
              shape = RoundedCornerShape(24.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
              ),
              border = BorderStroke(1.dp, Color.Gray),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(stringResource(R.string.request_has_been_sent))
            }
          }
          FriendshipStatus.FRIEND -> {
            Button(
              onClick = {
                navController.navigate("chat_room")
                onStartChat()
              },
              shape = RoundedCornerShape(24.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = stringResource(R.string.start_to_chat),
                color = Color.White
              )
            }
          }
        }
      }

      item {
        Text(
          text = stringResource(R.string.itineraries_created),
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }

      items(itineraries) { itinerary ->
        ItineraryCard(
          username = itinerary.username,
          title = itinerary.title,
          description = itinerary.description,
          onClick = { navController.navigate("detail_itinerary") }
        )
      }

      item {
        Text(
          text = stringResource(R.string.lihat_semua),
          fontSize = 14.sp,
          color = Color.Blue,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("itinerary_list") },
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
    }

    if (isCancelDialogVisible) {
      CancelFriendRequestDialog(
        onConfirm = {
          friendshipStatus = FriendshipStatus.NOT_FRIEND
          onCancelRequest()
          isCancelDialogVisible = false
        },
        onDismiss = {
          isCancelDialogVisible = false
        }
      )
    }
  }
}
