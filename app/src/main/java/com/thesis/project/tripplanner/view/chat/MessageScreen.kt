package com.thesis.project.tripplanner.view.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.data.Message
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
  navController: NavController,
  chatViewModel: ChatViewModel,
  currentUserId: String
) {
  val messages by chatViewModel.userChatPreviews.collectAsState()

  LaunchedEffect(currentUserId) {
    chatViewModel.loadUserChatPreviews(currentUserId)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.messages),
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
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    if (messages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "No messages available.", color = Color.Gray, fontSize = 16.sp)
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(messages) { message ->
          MessageItem(
            message = message,
            onMessageClick = { userId ->
              chatViewModel.markMessagesAsRead(currentUserId, userId)
              navController.navigate("chat_room/$userId")
            }
          )
        }
      }
    }
  }
}

@Composable
fun MessageItem(
  message: Message,
  onMessageClick: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .clickable { onMessageClick(message.userId) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = painterResource(R.drawable.ic_user_profile),
      contentDescription = "User Avatar",
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(text = message.username, fontWeight = FontWeight.Bold)
      Text(
        text = message.lastMessage,
        color = Color.Gray,
        maxLines = 1,
        modifier = Modifier.padding(top = 8.dp)
      )
    }
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.padding(top = 4.dp)
    ) {
      if (message.isUnread) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.Green)
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = formatDate(message.lastMessageDate.toLongOrNull() ?: 0),
        color = Color.Gray,
        fontSize = 12.sp
      )
    }
  }
}

fun formatDate(timestamp: Long): String {
  val date = Date(timestamp)
  val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
  return formatter.format(date)
}
