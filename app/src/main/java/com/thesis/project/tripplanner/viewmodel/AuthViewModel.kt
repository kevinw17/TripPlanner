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

  val auth: FirebaseAuth = FirebaseAuth.getInstance()
  val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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

  private val _isLoadingProfile = MutableStateFlow(false)
  val isLoadingProfile = _isLoadingProfile.asStateFlow()

  private val _targetUsername = MutableStateFlow(Utils.EMPTY)
  val targetUsername = _targetUsername.asStateFlow()

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

          val userId = auth.currentUser?.uid
          val defaultUsername = email.substringBefore("@")
          val defaultBio = "Anda bisa menambahkan bio Anda melalui edit profile"
          val defaultProfileImageUrl = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")

          if (userId != null) {
            val userProfileData = mapOf(
              "name" to defaultUsername,
              "bio" to defaultBio,
              "profileImageUrl" to defaultProfileImageUrl.toString()
            )

            firestore.collection("users").document(userId)
              .set(userProfileData, SetOptions.merge())
              .addOnSuccessListener {
                Log.d("AuthViewModel", "Default user profile created successfully")
                initializeUsernameFromEmail(email)
                _bio.value = defaultBio
                _profileImageUrl.value = defaultProfileImageUrl
              }
              .addOnFailureListener { exception ->
                Log.e("AuthViewModel", "Failed to create default user profile: ${exception.message}")
              }
          }
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

          val currentUserId = auth.currentUser?.uid
          val defaultUsername = email.substringBefore("@")
          val defaultBio = "Anda bisa menambahkan bio Anda melalui edit profile"
          val defaultProfileImageUrl = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")

          if (currentUserId != null) {
            val userProfileData = mapOf(
              "name" to defaultUsername,
              "bio" to defaultBio,
              "profileImageUrl" to defaultProfileImageUrl.toString()
            )

            firestore.collection("users").document(currentUserId)
              .set(userProfileData)
              .addOnSuccessListener {
                Log.d("AuthViewModel", "User profile created successfully")
                _username.value = defaultUsername
                _bio.value = defaultBio
                _profileImageUrl.value = defaultProfileImageUrl
              }
              .addOnFailureListener { exception ->
                Log.e("AuthViewModel", "Failed to create user profile: ${exception.message}")
                _authState.value = AuthState.Error("Failed to create user profile")
              }
          }
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
    _isLoadingProfile.value = true

    userId?.let { uid ->
      firestore.collection("users").document(uid).get()
        .addOnSuccessListener { document ->
          if (document.exists()) {
            _username.value = document.getString("name") ?: initializeUsernameFromEmail(auth.currentUser?.email)
            _bio.value = document.getString("bio") ?: "Anda bisa menambahkan bio Anda melalui edit profile"
            val uriString = document.getString("profileImageUrl")
            _profileImageUrl.value = if (!uriString.isNullOrEmpty()) {
              Uri.parse(uriString)
            } else {
              Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
            }
          } else {
            initializeUsernameFromEmail(auth.currentUser?.email)
            _bio.value = "Anda bisa menambahkan bio Anda melalui edit profile"
            _profileImageUrl.value = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
          }
          _isLoadingProfile.value = false
        }
        .addOnFailureListener {
          _authState.value = AuthState.Error("Failed to load user profile")
          _isLoadingProfile.value = false
          _profileImageUrl.value = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
        }
    } ?: run {
      _isLoadingProfile.value = false
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
      "profileImageUrl" to (_profileImageUrl.value?.toString() ?: Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}").toString())
    )

    firestore.collection("users").document(userId!!)
      .set(userProfileData, SetOptions.merge())
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
    _profileImageUrl.value = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
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

  fun loadUserProfileById(userId: String) {
    if (userId.isEmpty()) return

    Log.d("AuthViewModel", "Fetching user profile for userId: $userId")
    _isLoadingProfile.value = true

    firestore.collection("users").document(userId).get()
      .addOnSuccessListener { document ->
        if (document.exists()) {
          Log.d("AuthViewModel", "User profile fetched: ${document.data}")
          _targetUsername.value = document.getString("name") ?: "Unknown User"
          _bio.value = document.getString("bio") ?: "No bio available"
          val uriString = document.getString("profileImageUrl")
          _profileImageUrl.value = if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
          } else {
            Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
          }
        } else {
          Log.e("AuthViewModel", "User profile not found for userId: $userId")
          _targetUsername.value = "Unknown User"
          _bio.value = "No bio available"
          _profileImageUrl.value = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
        }
        _isLoadingProfile.value = false
      }
      .addOnFailureListener { exception ->
        Log.e("AuthViewModel", "Failed to fetch user profile: ${exception.message}")
        _targetUsername.value = "Unknown User"
        _bio.value = "No bio available"
        _profileImageUrl.value = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")
        _isLoadingProfile.value = false
      }
  }

  fun handleGoogleSignInSuccess(userId: String?, email: String?, displayName: String?) {
    if (userId == null || email == null) {
      Log.e("AuthViewModel", "Google Sign-In failed: User ID or email is null")
      _authState.value = AuthState.Error("Google Sign-In failed")
      return
    }

    val defaultBio = "Anda bisa menambahkan bio Anda melalui edit profile"
    val defaultProfileImageUrl = Uri.parse("android.resource://com.thesis.project.tripplanner/${R.drawable.ic_user_profile}")

    val userProfileData = mapOf(
      "name" to (displayName ?: email.substringBefore("@")),
      "bio" to defaultBio,
      "profileImageUrl" to defaultProfileImageUrl.toString()
    )

    firestore.collection("users").document(userId)
      .set(userProfileData, SetOptions.merge())
      .addOnSuccessListener {
        Log.d("AuthViewModel", "User profile created successfully for Google Sign-In")
        _username.value = displayName ?: email.substringBefore("@")
        _bio.value = defaultBio
        _profileImageUrl.value = defaultProfileImageUrl
        _authState.value = AuthState.Authenticated
      }
      .addOnFailureListener { exception ->
        Log.e("AuthViewModel", "Failed to create user profile for Google Sign-In: ${exception.message}")
        _authState.value = AuthState.Error("Failed to create user profile for Google Sign-In")
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