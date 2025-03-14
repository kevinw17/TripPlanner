package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.data.Itinerary
import com.thesis.project.tripplanner.view.dialog.CancelFriendRequestDialog
import com.thesis.project.tripplanner.view.dialog.DeleteFriendDialog
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard

@Composable
fun UserProfileContent(
  navController: NavController,
  username: String,
  profileImageUrl: String,
  bio: String,
  itinerariesCount: Int,
  friendsCount: Int,
  friendStatus: FriendshipStatus,
  itineraries: List<Itinerary>,
  isCancelDialogVisible: Boolean,
  isDeleteFriendDialogVisible: Boolean,
  onAddFriend: () -> Unit,
  onCancelRequest: () -> Unit,
  onDeleteFriendRequest: () -> Unit,
  onAcceptFriendRequest: () -> Unit,
  onStartChat: () -> Unit,
  onDeleteFriend: () -> Unit,
  onConfirmCancelRequest: () -> Unit,
  onDismissDialog: () -> Unit,
  paddingValues: PaddingValues
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
      .padding(paddingValues)
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.Start
  ) {

    item {
      Row {
        Column(
          modifier = Modifier
            .padding(start = 8.dp, top = 16.dp)
            .widthIn(max = 100.dp)
        ) {
          Box(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
          ) {
            Image(
              painter = rememberAsyncImagePainter(profileImageUrl),
              contentDescription = "User Avatar",
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          androidx.compose.material.Text(
            text = username,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
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
              androidx.compose.material.Text(
                text = "$itinerariesCount",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              androidx.compose.material.Text(
                text = stringResource(R.string.itineraries),
                fontSize = 18.sp,
                color = Color.Gray
              )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              androidx.compose.material.Text(
                text = "$friendsCount",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              androidx.compose.material.Text(
                text = stringResource(R.string.friends),
                fontSize = 18.sp,
                color = Color.Gray
              )
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
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }

    item {
      when (friendStatus) {
        FriendshipStatus.NOT_FRIEND -> {
          Button(
            onClick = onAddFriend,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFDFF9FF),
              contentColor = Color.Black
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Black)
          ) {
            Text(text = stringResource(R.string.add_friend))
          }
        }
        FriendshipStatus.REQUEST_SENT -> {
          Button(
            onClick = onCancelRequest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.LightGray,
              contentColor = Color.Black
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Black)
          ) {
            Text(text = stringResource(R.string.request_has_been_sent))
          }
        }
        FriendshipStatus.ACCEPT_REQUEST -> {
          Button(
            onClick = onAcceptFriendRequest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFDFF9FF),
              contentColor = Color.Black
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Black)
          ) {
            Text(text = stringResource(R.string.accept_request))
          }
        }
        FriendshipStatus.FRIEND -> {
          Row {
            Button(
              onClick = onStartChat,
              modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005FED),
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(24.dp),
              border = BorderStroke(1.dp, Color.Black)
            ) {
              Text(text = stringResource(R.string.start_to_chat))
            }
            Button(
              onClick = onDeleteFriendRequest,
              modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF0000),
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(24.dp),
              border = BorderStroke(1.dp, Color.Black)
            ) {
              Text(text = stringResource(R.string.delete_friend))
            }
          }
        }
      }
    }

    item {
      Text(
        text = stringResource(R.string.itineraries_created),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Black
      )
    }

    if (itineraries.isEmpty()) {
      item {
        Text(
          text = stringResource(R.string.no_itineraries_created),
          fontSize = 14.sp,
          color = Color.Gray,
          modifier = Modifier.fillMaxWidth(),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
    } else {
      items(itineraries) { itinerary ->
        ItineraryCard(
          username = itinerary.username,
          title = itinerary.title,
          description = itinerary.description,
          profileImageUrl = itinerary.profileImageUrl,
          onClick = {
            navController.navigate("detail_itinerary/${itinerary.userId}/${itinerary.itineraryId}")
          }
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
  }

  if (isCancelDialogVisible) {
    CancelFriendRequestDialog(
      onConfirm = onConfirmCancelRequest,
      onDismiss = onDismissDialog
    )
  }

  if (isDeleteFriendDialogVisible) {
    DeleteFriendDialog(
      onConfirm = onDeleteFriend,
      onDismiss = onDismissDialog
    )
  }
}