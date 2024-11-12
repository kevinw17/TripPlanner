package com.thesis.project.tripplanner.view.account

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.components.EditProfilePictureBottomSheet
import com.thesis.project.tripplanner.view.bottomnav.BottomNavigationBar
import com.thesis.project.tripplanner.view.dialog.RemovePictureDialog
import com.thesis.project.tripplanner.view.itinerary.ItineraryCard
import com.thesis.project.tripplanner.viewmodel.AuthState
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.ItineraryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  navController: NavController,
  onEditProfile: () -> Unit,
  onChangePassword: () -> Unit,
  authViewModel: AuthViewModel,
  itineraryViewModel: ItineraryViewModel
) {

  val username by authViewModel.username.collectAsState()
  val bio by authViewModel.bio.collectAsState()
  val itineraries by itineraryViewModel.itineraries.collectAsState()
  val itinerariesCount by itineraryViewModel.itineraryCount.collectAsState()
  val authState by authViewModel.authState.observeAsState()
  val friendsCount = 10
  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  var showOverlay by remember { mutableStateOf(false) }
  var showRemovePictureDialog by remember { mutableStateOf(false) }
  val isLoadingProfile by authViewModel.isLoadingProfile.collectAsState()
  val context = LocalContext.current
  val profileImageUrl by authViewModel.profileImageUrl.collectAsState()
  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    uri?.let { selectedImageUri ->
      authViewModel.saveProfileImageUri(selectedImageUri)
      authViewModel.loadUserProfile()
      Toast.makeText(
        context,
        context.getString(R.string.new_photo_profile_changed),
        Toast.LENGTH_SHORT
      ).show()
      coroutineScope.launch {
        scaffoldState.bottomSheetState.partialExpand()
      }
    }
  }

  LaunchedEffect(Unit) {
    authViewModel.loadUserProfile()
  }

  LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
    showOverlay = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
  }

  LaunchedEffect(authState) {
    if (authState is AuthState.Authenticated) {
      authViewModel.userId?.let { userId ->
        authViewModel.loadUserProfile()
        itineraryViewModel.loadItineraries(userId)
        authViewModel.loadProfileImageUri()
      }
    } else {
      itineraryViewModel.clearItineraries()
    }
  }

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
              painter = painterResource(R.drawable.ic_arrow_left),
              contentDescription = "Back",
              modifier = Modifier.size(24.dp)
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
    BottomSheetScaffold(
      scaffoldState = scaffoldState,
      sheetContent = {
        EditProfilePictureBottomSheet(
          onNewProfilePicture = {
            galleryLauncher.launch("image/*")
          },
          onRemoveProfilePicture = {
            showRemovePictureDialog = true
          }
        )
      },
      sheetPeekHeight = 0.dp
    ) { innerPadding ->
      if (isLoadingProfile) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }
      } else {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
              .padding(innerPadding)
              .padding(16.dp)
              .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                      painter = if (profileImageUrl != null) {
                        rememberAsyncImagePainter(profileImageUrl)
                      } else {
                        painterResource(R.drawable.ic_user_profile)
                      },
                      contentDescription = "User Avatar",
                      modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {
                          showOverlay = true
                          coroutineScope.launch {
                            delay(100)
                            scaffoldState.bottomSheetState.expand()
                          }
                        }
                    )
                  }
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
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

            if (itineraries.isEmpty()) {
              item {
                Text(
                  text = stringResource(R.string.no_itineraries_created),
                  fontSize = 14.sp,
                  color = Color.Black,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                  textAlign = TextAlign.Center
                )
              }
            } else {
              items(itineraries.take(2)) { itinerary ->
                ItineraryCard(
                  username = username,
                  title = itinerary.title,
                  description = itinerary.description,
                  profileImageUrl = itinerary.profileImageUrl,
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
                  textAlign = TextAlign.Center
                )
              }
            }
          }
          if (showOverlay) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() } }
            )
          }
        }
      }
    }
  }
  if (showRemovePictureDialog) {
    RemovePictureDialog(
      onConfirm = {
        authViewModel.removeProfileImage()
        showRemovePictureDialog = false
        coroutineScope.launch {
          scaffoldState.bottomSheetState.partialExpand()
          Toast.makeText(
            context,
            context.getString(R.string.success_remove_profile_picture),
            Toast.LENGTH_SHORT
          ).show()
        }
      },
      onDismiss = {
        showRemovePictureDialog = false
      }
    )
  }
}
