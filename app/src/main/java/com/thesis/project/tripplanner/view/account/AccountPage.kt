package com.thesis.project.tripplanner.view.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountPage(
  navController: NavController,
  onSignOut: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.account),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
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
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        ),
      )
    },
    bottomBar = {
      BottomNavigationBar(navController)
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      AccountOption(
        iconRes = R.drawable.ic_user_profile,
        label = stringResource(R.string.profile),
        onClick = { navController.navigate("profile") }
      )

      Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
      )

      AccountOption(
        iconRes = R.drawable.ic_friends,
        label = stringResource(R.string.friends),
        onClick = { navController.navigate("friends")
        }
      )

      Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
          .padding(vertical = 12.dp)
          .fillMaxWidth()
      )

      AccountOption(
        iconRes = R.drawable.ic_message,
        label = stringResource(R.string.messages),
        onClick = { navController.navigate("message") }
      )

      Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onSignOut,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        ),
        border = BorderStroke(
          width = 1.dp,
          color = Color.Black
        )
      ) {
        Text(text = stringResource(R.string.sign_out))
      }
    }
  }
}

@Composable
fun AccountOption(
  iconRes: Int,
  label: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .clickable(onClick = onClick),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier.size(32.dp)
      )
      Spacer(modifier = Modifier
        .width(16.dp)
        .height(48.dp))
      Text(text = label, fontSize = 18.sp)
    }
    Icon(
      painter = painterResource(R.drawable.ic_right_chevron),
      contentDescription = "Forward Arrow",
      modifier = Modifier.size(16.dp)
    )
  }
}
