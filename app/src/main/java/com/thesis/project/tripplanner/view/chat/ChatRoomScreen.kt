package com.thesis.project.tripplanner.view.chat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar

data class Message(
  val text: String,
  val time: String,
  val isSentByUser: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
  navController: NavController,
  messages: List<Message> = listOf(
    Message("abcdefghi...xyz", "10:03", false),
    Message("abcdefghijklmnopqrstuvwxyzaaaaaaaaaaaa", "10:05", true)
  ),
  onSendMessage: (String) -> Unit
) {
  var messageText by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              painter = painterResource(R.drawable.ic_user_profile),
              contentDescription = "User Avatar",
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Brian", fontWeight = FontWeight.Bold, fontSize = 20.sp)
          }
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
      ) {
        items(messages.reversed()) { message ->
          MessageBubble(message = message)
        }
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      ) {
        OutlinedTextField(
          value = messageText,
          onValueChange = { messageText = it },
          placeholder = { Text(text = "Message") },
          modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
          shape = RoundedCornerShape(24.dp)
        )
        IconButton(
          onClick = {
            if (messageText.isNotBlank()) {
              onSendMessage(messageText)
              messageText = ""
            }
          },
          modifier = Modifier.size(40.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.Gray
          )
        }
      }
    }
  }
}

@Composable
fun MessageBubble(message: Message) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = if (message.isSentByUser) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .background(
          color = if (message.isSentByUser) Color(0xFFDFF9FF) else Color(0xFFF2F2F2),
          shape = RoundedCornerShape(8.dp)
        )
        .padding(12.dp)
    ) {
      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = message.text,
          color = Color.Black,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = message.time,
          color = Color.Gray,
          fontSize = 10.sp
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ChatRoomScreenPreview() {
  val context = LocalContext.current
  ChatRoomScreen(
    navController = NavController(context = context),
    onSendMessage = { /* Handle send message */ }
  )
}
