package com.thesis.project.tripplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel: ViewModel() {

  private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
}

sealed class AuthState {
  object Authenticated: AuthState()
  object Unauthenticated: AuthState()
  object Loading: AuthState()
  data class Error(val message: String) : AuthState()
}