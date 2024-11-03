package com.thesis.project.tripplanner.view.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

data class Message(
  val username: String,
  val messagePreview: String,
  val date: String,
  val isOnline: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
  navController: NavController,
  messages: List<Message> = listOf(
    Message("Brian", "abcdefghi...xyz", "12 Oktober 2024", true),
    Message("Ian", "abcdefghi...xyz", "12 Oktober 2024", false),
    Message("Jack", "abcdefghi...xyz", "12 Oktober 2024", false),
    Message("Ryan", "abcdefghi...xyz", "12 Oktober 2024", true),
    Message("Charlie", "abcdefghi...xyz", "12 Oktober 2024", false),
    Message("David", "abcdefghi...xyz", "12 Oktober 2024", true),
    Message("Fred", "abcdefghi...xyz", "12 Oktober 2024", true),
    Message("Charlie", "abcdefghi...xyz", "12 Oktober 2024", false),
    Message("David", "abcdefghi...xyz", "12 Oktober 2024", true),
    Message("Fred", "abcdefghi...xyz", "12 Oktober 2024", true)
  )
) {
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
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back"
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(messages) { message ->
        MessageItem(navController, message)
      }
    }
  }
}

@Composable
fun MessageItem(
  navController: NavController,
  message: Message
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .clickable { navController.navigate("chat_room") },
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
      Text(text = message.messagePreview, color = Color.Gray, maxLines = 1, modifier = Modifier.padding(top = 8.dp))
    }
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.padding(top = 4.dp)
    ) {
      if (message.isOnline) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.Green)
            .padding(bottom = 4.dp)
        )
      }
      Text(
        text = message.date,
        color = Color.Gray,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 8.dp)
      )
    }
  }
}
