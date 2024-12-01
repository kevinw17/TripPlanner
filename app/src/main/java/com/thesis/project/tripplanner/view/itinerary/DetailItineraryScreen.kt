package com.thesis.project.tripplanner.view.itinerary

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.rememberAsyncImagePainter
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.DeleteItineraryDialog
import com.thesis.project.tripplanner.view.dialog.RecommendationDialog
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItineraryScreen(
  navController: NavController,
  itineraryViewModel: ItineraryViewModel,
  authViewModel: AuthViewModel,
  itineraryId: String,
  currentUserId: String
) {

  val selectedItinerary by itineraryViewModel.selectedItinerary.collectAsState()
  val comments by itineraryViewModel.comments.collectAsState()
  var isLiked by remember { mutableStateOf(false) }
  var isRecommended by remember { mutableStateOf(false) }
  var isDialogVisible by remember { mutableStateOf(false) }
  var showOverlay by remember { mutableStateOf(false) }
  var isDeleteDialogVisible by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  val context = LocalContext.current
  val profileImageUrl by authViewModel.profileImageUrl.collectAsState()

  LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
    showOverlay = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
  }

  LaunchedEffect(selectedItinerary) {
    selectedItinerary?.let {
      isLiked = it.likedBy.contains(currentUserId)
      isRecommended = it.recommendedBy.contains(currentUserId)
    }
  }

  LaunchedEffect(itineraryId) {
    itineraryViewModel.loadItineraryById(currentUserId, itineraryId)
    itineraryViewModel.loadComments(itineraryId)
  }

  Scaffold(topBar = {
    TopAppBar(
      title = {
        Text(
          text = stringResource(R.string.detail_itinerary),
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp)
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
      actions = {
        selectedItinerary?.let { itinerary ->
          if (itinerary.userId == authViewModel.userId) {
            IconButton(onClick = { isDeleteDialogVisible = true }) {
              Icon(
                painter = painterResource(R.drawable.ic_remove),
                contentDescription = "Delete Itinerary",
                modifier = Modifier.size(18.dp),
                tint = Color.Red
              )
            }
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
  }, bottomBar = { BottomNavigationBar(navController) }) { paddingValues ->
    BottomSheetScaffold(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color.White),
      scaffoldState = scaffoldState,
      sheetContent = {
        CommentBottomSheet(
          comments = comments,
          onSendComment = { commentText ->
            val username = authViewModel.username.value
            itineraryViewModel.addComment(
                itineraryId = itineraryId,
                username = username,
                commentText = commentText,
                profileImageUrl = profileImageUrl.toString(),
                userId = authViewModel.userId.orEmpty()
            )
          },
          currentUserId = authViewModel.userId.orEmpty(),
          profileImageUrl = profileImageUrl.toString(),
          onDeleteComment = { comment ->
            itineraryViewModel.deleteComment(itineraryId, comment)
          }
        )
      },
      sheetContainerColor = Color.White,
      sheetContentColor = Color.White,
      sheetPeekHeight = 0.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White)
          .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
      ) {
        selectedItinerary?.let { item ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable {
                navController.navigate("user_profile_screen/${item.userId}")
              }
          ) {
            Image(
              painter = if (item.profileImageUrl != null) {
                  rememberAsyncImagePainter(item.profileImageUrl)
              } else {
                  painterResource(R.drawable.ic_user_profile)
              },
              contentDescription = "User Avatar",
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = item.username,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = item.title,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
          Text(
            text = "Start Date: ${item.startDate}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
          )
          Text(
            text = "End Date: ${item.endDate}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              painter = painterResource(R.drawable.ic_like),
              contentDescription = "Like",
              tint = if (isLiked) Color.Red else Color.Black,
              modifier = Modifier
                .size(20.dp)
                .clickable {
                  isLiked = !isLiked
                  itineraryViewModel.updateLikeCount(
                    itineraryId, currentUserId, isLiked
                  )
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
              painter = painterResource(R.drawable.ic_comment),
              contentDescription = "Share",
              modifier = Modifier
                .size(20.dp)
                .clickable {
                  showOverlay = true
                  coroutineScope.launch {
                    kotlinx.coroutines.delay(100)
                    scaffoldState.bottomSheetState.expand()
                  }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
              painter = painterResource(R.drawable.ic_recommend),
              contentDescription = "Send",
              modifier = Modifier
                .size(20.dp)
                .clickable {
                  isDialogVisible = true
                }
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "${item.likeCount} Likes",
            fontSize = 14.sp
          )
          Text(
            text = "Recommended by ${item.recommendationCount} people",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = stringResource(R.string.description),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          Text(
            text = item.description,
            fontSize = 14.sp,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(text = stringResource(R.string.view_all_comments),
            color = Color.Blue,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
              showOverlay = true
              coroutineScope.launch {
                kotlinx.coroutines.delay(100)
                scaffoldState.bottomSheetState.expand()
              }
            })
        }
      }
      if (showOverlay) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() } })
      }
      if (isDialogVisible) {
        RecommendationDialog(
          onConfirm = {
            itineraryViewModel.updateRecommendationCount(currentUserId, itineraryId, isRecommended)
            isDialogVisible = false
            Toast.makeText(context, R.string.itinerary_recommended, Toast.LENGTH_SHORT).show()
          },
          onDismiss = { isDialogVisible = false })
      }
      if (isDeleteDialogVisible) {
        DeleteItineraryDialog(
          onConfirm = {
            isDeleteDialogVisible = false
            itineraryViewModel.deleteItinerary(itineraryId, currentUserId) {
              Toast.makeText(
                context, context.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT
              ).show()
              navController.navigate("profile")
            }
          },
          onDismiss = { isDeleteDialogVisible = false })
      }
    }
  }
}