package com.thesis.project.tripplanner.view.login_register

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.utils.Utils
import com.thesis.project.tripplanner.view.google_sign_in.GoogleAuthUiClient
import com.thesis.project.tripplanner.viewmodel.AuthState
import com.thesis.project.tripplanner.viewmodel.AuthViewModel
import com.thesis.project.tripplanner.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel,
  signInViewModel: SignInViewModel = viewModel()
) {

  var email by remember { mutableStateOf(Utils.EMPTY) }
  var password by remember { mutableStateOf(Utils.EMPTY) }
  val authState = authViewModel.authState.observeAsState()
  val signInState by signInViewModel.state.collectAsState()
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val oneTapClient = Identity.getSignInClient(context)
  val googleAuthUiClient = GoogleAuthUiClient(context, oneTapClient)

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartIntentSenderForResult()
  ) { result ->
    if (result.resultCode == -1) {
      val intent = result.data
      intent?.let {
        coroutineScope.launch {
          val signInResult = googleAuthUiClient.signInWithIntent(intent)
          signInViewModel.onSignInResult(signInResult)
        }
      }
    }
  }

  LaunchedEffect(authState.value) {
    when(authState.value) {
      is AuthState.Authenticated -> navController.navigate("home")
      is AuthState.Error -> Toast.makeText(
        context,
        (authState.value as AuthState.Error).message,
        Toast.LENGTH_SHORT
      ).show()
      else -> Unit
    }
  }

  LaunchedEffect(signInState) {
    if (signInState.isSignInSuccessful) {
      authViewModel.setAuthenticated()
      val user = googleAuthUiClient.getSignedInUser()
      if (user != null) {
        val userId = user.userId
        val googleEmail = authViewModel.auth.currentUser?.email
        val username = googleEmail?.substringBefore("@") ?: "Pengguna Baru"
        val profilePictureUrl = user.profilePictureUrl
          ?: "android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}"
        val defaultBio = "Anda bisa menambahkan bio Anda melalui edit profile"

        authViewModel.firestore.collection("users").document(userId).get()
          .addOnSuccessListener { document ->
            if (!document.exists()) {
              val userProfileData = mapOf(
                "name" to username,
                "bio" to defaultBio,
                "profileImageUrl" to profilePictureUrl
              )

              authViewModel.firestore.collection("users").document(userId)
                .set(userProfileData)
                .addOnSuccessListener {
                  Toast.makeText(
                    context,
                    context.getString(R.string.google_sign_in_berhasil),
                    Toast.LENGTH_SHORT
                  ).show()
                  navController.navigate("home")
                }
                .addOnFailureListener {
                  Toast.makeText(
                    context,
                    context.getString(R.string.failed_save_user_account),
                    Toast.LENGTH_SHORT
                  ).show()
                }
              authViewModel.loadUserProfile()
            } else {
              authViewModel.loadUserProfile()
              navController.navigate("home")
            }
          }
          .addOnFailureListener {
            Toast.makeText(
              context,
              context.getString(R.string.failed_fetch_user_document),
              Toast.LENGTH_SHORT
            ).show()
          }
      }
    } else if (signInState.signInError != null) {
      Toast.makeText(
        context,
        context.getString(R.string.google_sign_in_failed, signInState.signInError),
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  Box(
    modifier = Modifier
      .background(color = Color(0xFFDFF9FF))
      .fillMaxSize()
  ) {
    Column(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.login_to_trip_planner_app),
        fontSize = 24.sp
      )

      Spacer(modifier = Modifier.height(64.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 54.dp)
      ) {
        Text(
          text = stringResource(R.string.email),
          color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
          ),
          value = email,
          onValueChange = {
            email = it
          },
          placeholder = {
            Text(text = stringResource(R.string.masukkan_email))
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 54.dp)
      ) {
        Text(
          text = stringResource(R.string.password),
          color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        PasswordField(
          password = password,
          onPasswordChange = { password = it },
          isError = false
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = { authViewModel.login(email, password) },
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
          width = 1.dp,
          color = Color.Black
        )
      ) {
        Text(text = stringResource(R.string.login))
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(R.string.atau),
        color = Color.Black
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = {
          coroutineScope.launch {
            val intentSender = googleAuthUiClient.signIn()
            intentSender?.let {
              launcher.launch(IntentSenderRequest.Builder(it).build())
            } ?: run {
              Toast.makeText(context,
                context.getString(R.string.google_sign_in_failed_to_initiate), Toast.LENGTH_SHORT).show()
            }
          }
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
          width = 1.dp,
          color = Color.Black
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 54.dp)
      ) {
        Icon(
          painter = painterResource(R.drawable.ic_google),
          contentDescription = "Google Sign-In",
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.login_dengan_akun_google))
      }

      Spacer(modifier = Modifier.height(8.dp))

      TextButton(
        onClick = {
          navController.navigate("register")
        }
      ) {
        Text(
          text = stringResource(R.string.not_registered_hyperlink),
          color = Color(0xFF005FED)
        )
      }
    }
  }
}