package com.thesis.project.tripplanner.view.google_sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.thesis.project.tripplanner.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
  private val context: Context,
  private val oneTapClient: SignInClient
) {
  private val auth = Firebase.auth

  suspend fun signIn(): IntentSender? {
    return try {
      val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
      result.pendingIntent?.intentSender
    } catch (e: Exception) {
      Toast.makeText(context, "Google Sign-In failed to initiate: ${e.message}", Toast.LENGTH_SHORT).show()
      if (e is CancellationException) throw e
      null
    }
  }

  suspend fun signInWithIntent(intent: Intent): SignInResult {
    val credential = oneTapClient.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    return try {
      val user = auth.signInWithCredential(googleCredentials).await().user
      SignInResult(
        data = user?.run {
          displayName?.let {
            UserData(
              userId = uid, username = it, profilePictureUrl = photoUrl?.toString()
            )
          }
        },
        errorMessage = null
      )
    } catch (e: Exception) {
      e.printStackTrace()
      if(e is CancellationException) throw e
      SignInResult(
        data = null,
        errorMessage = e.message
      )
    }
  }

  fun getSignedInUser(): UserData? = auth.currentUser?.run {
    displayName?.let {
      UserData(
        userId = uid, username = it, profilePictureUrl = photoUrl?.toString()
      )
    }
  }

  private fun buildSignInRequest(): BeginSignInRequest {
    return BeginSignInRequest.Builder()
      .setGoogleIdTokenRequestOptions(
        GoogleIdTokenRequestOptions.builder()
          .setSupported(true)
          .setFilterByAuthorizedAccounts(false)
          .setServerClientId(context.getString(R.string.web_client_id))
          .build()
      )
      .setAutoSelectEnabled(true)
      .build()
  }
}