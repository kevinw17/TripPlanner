package com.thesis.project.tripplanner.view.itinerary

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.RecommendationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItineraryScreen(
    navController: NavController
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(10) }
    var isDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Itinerary", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Profile Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_user_profile),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "andy123", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Liburan ke Bali", fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            Text(text = "Start Date: 10 Oktober 2024", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            Text(text = "End Date: 12 Oktober 2024", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // Social Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_like),
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            isLiked = !isLiked
                            likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                        }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(painter = painterResource(R.drawable.ic_comment), contentDescription = "Share", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_recommend),
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { isDialogVisible = true }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$likeCount Likes", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text(text = "Description:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = """
                    Jalan-jalan ke Bali sangat seru!! Jalan-jalan ke Bali sangat seru!!
                    Jalan-jalan ke Bali sangat seru!! Jalan-jalan ke Bali sangat seru!!
                    Jalan-jalan ke Bali sangat seru!! Jalan-jalan ke Bali sangat seru!!
                    Jalan-jalan ke Bali sangat seru!! Jalan-jalan ke Bali sangat seru!!
                """.trimIndent(),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // View All Comments
            Text(
                text = stringResource(R.string.view_all_comments),
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Handle view comments */ }
            )
        }
        if (isDialogVisible) {
            RecommendationDialog(
                onConfirm = {
                    isDialogVisible = false
                    Toast.makeText(context, "Itinerary berhasil direkomendasikan", Toast.LENGTH_SHORT).show()
                },
                onDismiss = { isDialogVisible = false }
            )
        }
    }
}
