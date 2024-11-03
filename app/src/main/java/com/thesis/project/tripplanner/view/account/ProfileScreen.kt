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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.thesis.project.tripplanner.view.explore.Itinerary
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  navController: NavController,
  username: String = "andy123",
  bio: String = "I love travelling...",
  itinerariesCount: Int = 1,
  friendsCount: Int = 10,
  onEditProfile: () -> Unit,
  onChangePassword: () -> Unit,
  itineraries: List<Itinerary> = listOf(
    Itinerary("andy123", "Liburan ke Bali", "Jalan-jalan ke Bali sangat seru!!")
  )
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.profile),
            fontWeight = FontWeight.Bold, fontSize = 20.sp
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
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Button(
            onClick = onEditProfile,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFDFF9FF),
              contentColor = Color.Black
            ),
            border = BorderStroke(
              width = 1.dp,
              color = Color.Black
            ),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = stringResource(R.string.edit_profile))
          }

          Button(
            onClick = onChangePassword,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFDFF9FF),
              contentColor = Color.Black
            ),
            border = BorderStroke(
              width = 1.dp,
              color = Color.Black
            ),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = stringResource(R.string.change_password))
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
  }
}


