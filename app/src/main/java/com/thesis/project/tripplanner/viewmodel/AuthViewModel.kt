package com.thesis.project.tripplanner.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {

  private val auth: FirebaseAuth = FirebaseAuth.getInstance()
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

  private val _authState = MutableLiveData<AuthState?>()
  val authState: MutableLiveData<AuthState?> = _authState
  var isNewUser = false

  val userId: String?
    get() = auth.currentUser?.uid

  private val _username = MutableStateFlow(Utils.EMPTY)
  val username = _username.asStateFlow()

  private val _bio = MutableStateFlow(Utils.EMPTY)
  val bio = _bio.asStateFlow()

  private val _profileImageUrl = MutableStateFlow<Uri?>(null)
  val profileImageUrl = _profileImageUrl.asStateFlow()

  init {
    checkAuthStatus()
  }

  private fun checkAuthStatus() {
    if(auth.currentUser == null) {
      _authState.value = AuthState.Unauthenticated
    } else {
      _authState.value = AuthState.Authenticated
      initializeUsernameFromEmail(auth.currentUser?.email)
      loadUserProfile()
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
          initializeUsernameFromEmail(email)
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
          initializeUsernameFromEmail(email)
        } else {
          _authState.value = AuthState.Error("Autentikasi gagal")
        }
      }
  }

  fun signOut() {
    auth.signOut()
    _authState.value = AuthState.Unauthenticated
    clearUserData()
  }

  fun resetErrorState() {
    _authState.value = null
  }

  fun setAuthenticated() {
    _authState.value = AuthState.Authenticated
  }

  fun loadUserProfile() {
    userId?.let { uid ->
      firestore.collection("users").document(uid).get()
        .addOnSuccessListener { document ->
          if (document.exists()) {
            _username.value = document.getString("name") ?: initializeUsernameFromEmail(auth.currentUser?.email).toString()
            _bio.value = document.getString("bio") ?: "Anda bisa menambahkan bio Anda melalui edit profile"
          } else {
            initializeUsernameFromEmail(auth.currentUser?.email)
            _bio.value = "Anda bisa menambahkan bio Anda melalui edit profile"
          }
        }
        .addOnFailureListener {
          _authState.value = AuthState.Error("Failed to load user profile")
        }
    }
  }


  fun updateUserProfile(bio: String) {
    if (userId == null) {
      _authState.value = AuthState.Error("User not authenticated")
      return
    }

    val userProfileData = mapOf(
      "name" to _username.value,
      "bio" to bio,
      "profileImageUrl" to _profileImageUrl.value?.toString()
    )

    firestore.collection("users").document(userId!!)
      .set(userProfileData)
      .addOnSuccessListener {
        _bio.value = bio
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

  private fun initializeUsernameFromEmail(email: String?): String {
    val username = email?.substringBefore("@") ?: Utils.EMPTY
    _username.value = username
    return username
  }

  private fun clearUserData() {
    _username.value = Utils.EMPTY
    _bio.value = Utils.EMPTY
    isNewUser = false
  }

  fun saveProfileImageUri(uri: Uri) {
    val uriString = uri.toString()
    userId?.let { uid ->
      val userRef = firestore.collection("users").document(uid)
      userRef.get().addOnSuccessListener { document ->
        if (document.exists()) {
          userRef.update("profileImageUrl", uriString)
            .addOnSuccessListener {
              _profileImageUrl.value = uri
            }
            .addOnFailureListener { exception ->
              exception.printStackTrace()
            }
        } else {
          userRef.set(mapOf("profileImageUrl" to uriString))
            .addOnSuccessListener {
              _profileImageUrl.value = uri
            }
            .addOnFailureListener { exception ->
              exception.printStackTrace()
            }
        }
      }.addOnFailureListener { exception ->
        exception.printStackTrace()
      }
    }
  }


  fun loadProfileImageUri() {
    userId?.let { uid ->
      firestore.collection("users").document(uid).get()
        .addOnSuccessListener { document ->
          val uriString = document.getString("profileImageUrl")
          _profileImageUrl.value = uriString?.let { Uri.parse(it) }
        }
        .addOnFailureListener { exception ->
          exception.printStackTrace()
        }
    }
  }

  fun removeProfileImage() {
    userId?.let { uid ->
      val userRef = firestore.collection("users").document(uid)
      userRef.update("profileImageUrl", null)
        .addOnSuccessListener {
          _profileImageUrl.value = null
        }
        .addOnFailureListener { exception ->
          exception.printStackTrace()
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