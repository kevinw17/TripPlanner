package com.thesis.project.tripplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel: ViewModel() {

  private val auth: FirebaseAuth = FirebaseAuth.getInstance()
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

  private val _authState = MutableLiveData<AuthState?>()
  val authState: MutableLiveData<AuthState?> = _authState
  var isNewUser = false

  init {
    checkAuthStatus()
  }

  private fun checkAuthStatus() {
    if(auth.currentUser == null) {
      _authState.value = AuthState.Unauthenticated
    } else {
      _authState.value = AuthState.Authenticated
    }
  }

  fun login(
    email: String,
    password: String
  ) {

    if(email.isEmpty() || password.isEmpty()) {
      _authState.value = AuthState.Error("Email atau password tidak boleh kosong")
      return
    }

    _authState.value = AuthState.Loading
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if(task.isSuccessful) {
          _authState.value = AuthState.Authenticated
        } else {
          _authState.value = AuthState.Error("Email atau password salah")
        }
      }
  }

  fun register(
    email: String,
    password: String,
    confirmPassword: String
  ) {

    if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      _authState.value = AuthState.Error("Email atau password tidak boleh kosong")
      return
    }

    if(password.length < 5) {
      _authState.value = AuthState.Error("Password harus minimal 5 karakter")
      return
    }

    if(password != confirmPassword) {
      _authState.value = AuthState.Error("Konfirmasi Password gagal")
      return
    }

    _authState.value = AuthState.Loading
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if(task.isSuccessful) {
          isNewUser = true
          _authState.value = AuthState.Authenticated
        } else {
          _authState.value = AuthState.Error("Autentikasi gagal")
        }
      }
  }

  fun signOut() {
    auth.signOut()
    _authState.value = AuthState.Unauthenticated
  }

  fun resetErrorState() {
    _authState.value = null
  }

  fun setAuthenticated() {
    _authState.value = AuthState.Authenticated
  }

  fun updateUserProfile(name: String, bio: String) {
    val userId = auth.currentUser?.uid
    if (userId == null) {
      _authState.value = AuthState.Error("User not authenticated")
      return
    }

    val userProfileData = mapOf(
      "name" to name,
      "bio" to bio
    )

    firestore.collection("users").document(userId)
      .set(userProfileData)
      .addOnSuccessListener {
        _authState.value = AuthState.ProfileUpdated("Profile updated successfully")
      }
      .addOnFailureListener {
        _authState.value = AuthState.Error("Failed to update profile")
      }
  }

  fun changePassword(
    auth: FirebaseAuth,
    oldPassword: String,
    newPassword: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val user = auth.currentUser
    if (user == null) {
      onError("User not authenticated.")
      return
    }

    val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
    user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
      if (reauthTask.isSuccessful) {
        user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
          if (updateTask.isSuccessful) {
            onSuccess()
          } else {
            onError("Failed to update password.")
          }
        }
      } else {
        onError("Old password is incorrect.")
      }
    }
  }
}

sealed class AuthState {
  object Authenticated: AuthState()
  object Unauthenticated: AuthState()
  object Loading: AuthState()
  data class Error(val message: String) : AuthState()
  data class ProfileUpdated(val message: String) : AuthState()
}