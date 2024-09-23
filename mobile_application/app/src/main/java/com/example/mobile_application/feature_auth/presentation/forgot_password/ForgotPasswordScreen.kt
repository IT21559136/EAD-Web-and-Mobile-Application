package com.example.mobile_application.feature_auth.presentation.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column(Modifier.padding(16.dp)) {
                Text(text = "Forgot Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Please enter an email address that you had registered with, so that we can send you a password reset link",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    ) { innerPadding ->
        // Use the innerPadding in the content of the Scaffold
        ForgotPasswordScreenContent(
            modifier = Modifier.padding(innerPadding),
            onClickForgotPassword = {
                Toast.makeText(
                    context,
                    "This API does not provide an endpoint for sending password reset link, just login with the credentials provided in the README file",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}

@Composable
private fun ForgotPasswordScreenContent(
    modifier: Modifier = Modifier,
    onClickForgotPassword: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(64.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {
                    // Handle email input change
                },
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                ),
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClickForgotPassword,
                shape = RoundedCornerShape(8)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), text = "Continue", textAlign = TextAlign.Center
                )
            }
        }
    }
}
