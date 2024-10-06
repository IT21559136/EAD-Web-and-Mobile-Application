package com.example.mobile_application.feature_auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mobile_application.core.domain.model.TextFieldState
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.core.presentation.ui.theme.poppins
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_auth.presentation.login.LoginState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // States from ViewModel
    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value
    val confirmPwdState = viewModel.confirmPwdState.value
    val emailState = viewModel.emailState.value
    val loginState = viewModel.loginState.value

    // SnackbarHostState to manage snackbar behavior
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Handle events from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is UiEvents.NavigateEvent -> {
                    navController.navigate(event.route) {
                        popUpTo("signup") { inclusive = true } // Optional back stack handling
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding->
             RegisterScreenContent(
                modifier = Modifier.fillMaxWidth().padding(),
                usernameState = usernameState,
                passwordState = passwordState,
                confirmPwdState = confirmPwdState,
                emailState = emailState,
                loginState = loginState,
                onUserNameTextChange = { viewModel.setUsername(it) },
                onPasswordTextChange = { viewModel.setPassword(it) },
                onConfirmPwdTextChange = { viewModel.setConfirmPassword(it) },
                onEmailTextChange = {viewModel.setEmail(it)},
                onClickSignUp = {
                    keyboardController?.hide()
                    viewModel.registerUser()
                }
            )
        }
}

@Composable
private fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    confirmPwdState: TextFieldState,
    emailState: TextFieldState,
    loginState: LoginState,
    onUserNameTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onConfirmPwdTextChange: (String) -> Unit,
    onEmailTextChange: (String) -> Unit,
    onClickSignUp: () -> Unit,
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(64.dp))
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Getting Started", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Create an account to continue with your shopping",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        item {
           Spacer(modifier = Modifier.height(64.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = usernameState.text,
                onValueChange = onUserNameTextChange,
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true
                ),
                maxLines = 1,
                singleLine = true,
                isError = usernameState.error != null
            )
            if (usernameState.error != null) {
                Text(
                    text = usernameState.error ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailState.text,
                onValueChange = onEmailTextChange,
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Email,
                ),
                maxLines = 1,
                singleLine = true,
                isError = emailState.error != null
            )
            if (emailState.error != null) {
                Text(
                    text = emailState.error ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordState.text,
                onValueChange = onPasswordTextChange,
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Password,
                ),
                maxLines = 1,
                singleLine = true,
                isError = passwordState.error != null
            )
            if (passwordState.error != null) {
                Text(
                    text = passwordState.error ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPwdState.text,
                onValueChange = onConfirmPwdTextChange,
                label = { Text(text = "Confirm Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Password,
                ),
                maxLines = 1,
                singleLine = true,
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onClickSignUp,
                shape = RoundedCornerShape(8),
                enabled = !loginState.isLoading
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), text = "Sign Up", textAlign = TextAlign.Center
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onClickSignUp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(
                            style = SpanStyle(color = YellowMain, fontWeight = FontWeight.Bold)
                        ) {
                            append("Sign In")
                        }
                    },
                    fontFamily = poppins,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (loginState.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
