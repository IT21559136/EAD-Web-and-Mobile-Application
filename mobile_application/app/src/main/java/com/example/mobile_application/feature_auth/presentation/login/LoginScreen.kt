package com.example.mobile_application.feature_auth.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mobile_application.core.domain.model.TextFieldState
import com.example.mobile_application.core.util.UiEvents
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // States from ViewModel
    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, // Bind the SnackbarHostState to the Scaffold
        topBar = {
            Column(
                Modifier.padding(16.dp), verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Welcome Back", fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                Text(
                    text = "Login to your account to continue shopping",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }
    ) { padding ->
        LoginScreenContent(
            modifier = Modifier.padding(padding),
            usernameState = usernameState,
            passwordState = passwordState,
            rememberMeState = rememberMeState,
            loginState = loginState,
            onUserNameTextChange = { viewModel.setUsername(it) },
            onPasswordTextChange = { viewModel.setPassword(it) },
            onRememberMeClicked = { viewModel.setRememberMe(it) },
            onClickForgotPassword = { navController.navigate("forgot_password") },
            onClickDontHaveAccount = { navController.navigate("register") },
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
    rememberMeState: Boolean,
    loginState: LoginState,
    onUserNameTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onRememberMeClicked: (Boolean) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickDontHaveAccount: () -> Unit,
    onClickSignIn: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = usernameState.text,
            onValueChange = onUserNameTextChange,
            label = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            isError = usernameState.error != null
        )
        if (usernameState.error != null) {
            Text(text = usernameState.error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordState.text,
            onValueChange = onPasswordTextChange,
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            isError = passwordState.error != null
        )
        if (passwordState.error != null) {
            Text(text = passwordState.error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMeState, onCheckedChange = onRememberMeClicked)
                Text(text = "Remember me")
            }
            TextButton(onClick = onClickForgotPassword) {
                Text(text = "Forgot password?")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onClickSignIn,
            enabled = !loginState.isLoading
        ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onClickDontHaveAccount) {
            Text(text = "Don't have an account? Sign Up")
        }

        if (loginState.isLoading) {
            CircularProgressIndicator()
        }
    }
}
