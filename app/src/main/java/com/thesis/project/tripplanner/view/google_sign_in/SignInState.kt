package com.thesis.project.tripplanner.view.google_sign_in

data class SignInState(
  val isSignInSuccessful: Boolean = false,
  val signInError: String? = null
)