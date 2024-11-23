package com.thesis.project.tripplanner.view.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.thesis.project.tripplanner.data.Chat
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
  navController: NavController,
  otherUserId: String,
  chatViewModel: ChatViewModel,
  currentUserId: String
) {
  val chatMessages by chatViewModel.chatMessages.collectAsState()
  var otherUserName by remember { mutableStateOf(Utils.EMPTY) }
  var messageText by remember { mutableStateOf(Utils.EMPTY) }
  val listState = rememberLazyListState()

  LaunchedEffect(otherUserId, currentUserId) {
    chatViewModel.loadChatMessages(currentUserId, otherUserId)
    chatViewModel.fetchUserName(otherUserId) { name ->
      otherUserName = name.orEmpty()
    }
  }

  LaunchedEffect(chatMessages) {
    if (chatMessages.isNotEmpty()) {
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.ic_user_profile), contentDescription = "User Avatar", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = otherUserName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
          }
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(painter = painterResource(R.drawable.ic_arrow_left), contentDescription = "Back", modifier = Modifier.size(24.dp))
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    bottomBar = { BottomNavigationBar(navController) }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        var previousDate: String? = null

        chatMessages.forEach { message ->
          val currentDate = dateFormat.format(Date(message.timestamp))

          if (currentDate != previousDate) {
            item {
              DateLabel(currentDate)
            }
            previousDate = currentDate
          }

          item {
            MessageBubble(message = message)
          }
        }
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      ) {
        androidx.compose.material3.OutlinedTextField(
          value = messageText,
          onValueChange = { messageText = it },
          placeholder = { Text(text = stringResource(R.string.message)) },
          modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEEF3F4),
            unfocusedContainerColor = Color(0xFFEEF3F4),
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
          shape = RoundedCornerShape(12.dp)
        )
        Button(
          onClick = {
            if (messageText.isNotBlank()) {
              chatViewModel.sendMessage(currentUserId, otherUserId, messageText)
              messageText = Utils.EMPTY
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEEF3F4),
            contentColor = Color.Black
          ),
          border = BorderStroke(
            width = 1.dp,
            color = Color.Black
          ),
          modifier = Modifier.weight(0.3f)
        ) {
          Text(text = stringResource(R.string.send))
        }
      }
    }
  }
}

@Composable
fun MessageBubble(message: Chat) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = if (message.isSentByUser) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .widthIn(max = if (message.isSentByUser) 330.dp else 300.dp)
        .background(
          color = if (message.isSentByUser) Color(0xFFDFF9FF) else Color(0xFFF2F2F2),
          shape = RoundedCornerShape(8.dp)
        )
        .padding(12.dp)
    ) {
      Column(horizontalAlignment = Alignment.Start) {
        Text(
          text = message.text,
          color = Color.Black,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = formatTimestamp(message.timestamp),
          color = Color.Gray,
          fontSize = 10.sp
        )
      }
    }
  }
}

@Composable
fun DateLabel(date: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.Center
  ) {
    Text(
      text = date,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.Gray
    )
  }
}

fun formatTimestamp(timestamp: Long): String {
  val date = Date(timestamp)
  val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
  return formatter.format(date)
}
