package com.thesis.project.tripplanner.presentation.sign_in

data class SignInState(
  val isSignInSuccessful: Boolean = false,
  val signInError: String? = null
)
