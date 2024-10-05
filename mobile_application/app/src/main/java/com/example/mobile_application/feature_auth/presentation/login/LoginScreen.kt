package com.example.mobile_application.feature_auth.presentation.login

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // States from ViewModel
    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value
    val emailState = viewModel.emailState.value
    val rememberMeState = viewModel.rememberMeState.value
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
                        popUpTo("login") { inclusive = true } // Optional back stack handling
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding->
            LoginScreenContent(
                modifier = Modifier.padding(padding),
                usernameState = usernameState,
                passwordState = passwordState,
                emailState = emailState,
                rememberMeState = rememberMeState,
                loginState = loginState,
                onUserNameTextChange = { viewModel.setUsername(it) },
                onPasswordTextChange = { viewModel.setPassword(it) },
                onEmailTextChange = {viewModel.setEmail(it)},
                onRememberMeClicked = { viewModel.setRememberMe(it) },
                onClickForgotPassword = { navController.navigate("forgot_password") },
                onClickDontHaveAccount = { navController.navigate("signup") },
                onClickSignIn = {
                    keyboardController?.hide()
                    viewModel.loginUser()
                }
            )
    }
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    emailState:TextFieldState,
    rememberMeState: Boolean,
    loginState: LoginState,
    onUserNameTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onEmailTextChange: (String) -> Unit,
    onRememberMeClicked: (Boolean) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickDontHaveAccount: () -> Unit,
    onClickSignIn: () -> Unit
) {
    Box(
        modifier.fillMaxSize(), // Fills the entire screen
        contentAlignment = Alignment.Center // Centers the content vertically and horizontally
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Login to your account to continue shopping",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(64.dp))

                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = usernameState.text,
                        onValueChange = onUserNameTextChange,
                        label = { Text(text = "Username") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
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
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordState.text,
                        onValueChange = onPasswordTextChange,
                        label = { Text(text = "Password") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
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
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailState.text,
                        onValueChange = onEmailTextChange,
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
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
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = rememberMeState, onCheckedChange = onRememberMeClicked)
                        Text(text = "Remember me", fontSize = 12.sp)
                    }
                    TextButton(onClick = onClickForgotPassword) {
                        Text(text = "Forgot password?")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onClickSignIn,
                    shape = RoundedCornerShape(8),
                    enabled = !loginState.isLoading
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), text = "Sign In", textAlign = TextAlign.Center
                    )
                }
            }


            item {
                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = onClickDontHaveAccount,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account?")
                            append(" ")
                            withStyle(
                                style = SpanStyle(color = YellowMain, fontWeight = FontWeight.Bold)
                            ) {
                                append("Sign Up")
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
}
