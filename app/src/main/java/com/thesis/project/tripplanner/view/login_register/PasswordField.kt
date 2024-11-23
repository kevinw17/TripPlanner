package com.thesis.project.tripplanner.view.login_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.utils.Utils

@Composable
fun PasswordField(
  password: String,
  onPasswordChange: (String) -> Unit,
  isError: Boolean = false,
  errorMessage: String = Utils.EMPTY
) {
  var passwordVisible by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = password,
    onValueChange = onPasswordChange,
    placeholder = {
      Text(text = stringResource(R.string.masukkan_password))
    },
    singleLine = true,
    modifier = Modifier
      .fillMaxWidth(),
    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    trailingIcon = {
      if (password.isNotEmpty()) {
        val image = if (passwordVisible) {
          painterResource(R.drawable.ic_eye)
        } else {
          painterResource(R.drawable.ic_eye_hidden)
        }

        Icon(
          painter = image,
          contentDescription = if (passwordVisible) "Munculkan password" else "Sembunyikan password",
          modifier = Modifier
            .size(18.dp)
            .clickable { passwordVisible = !passwordVisible }
        )
      }
    },
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = Color.White,
      unfocusedContainerColor = Color.White,
      focusedBorderColor = Color.Black,
      unfocusedBorderColor = Color.Black,
      focusedLabelColor = Color.Black,
      unfocusedLabelColor = Color.Black,
      errorContainerColor = Color.White
    ),
    isError = isError,
  )

  if (isError) {
    Text(
      text = errorMessage,
      color = Color.Red,
      fontSize = 12.sp,
      modifier = Modifier.padding(top = 4.dp)
    )
  }
}