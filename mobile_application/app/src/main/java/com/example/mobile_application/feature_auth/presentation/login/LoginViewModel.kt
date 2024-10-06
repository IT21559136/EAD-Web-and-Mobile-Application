package com.example.mobile_application.feature_auth.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.core.domain.model.TextFieldState
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _usernameState = mutableStateOf(TextFieldState(text = "jana"))
    val usernameState: State<TextFieldState> = _usernameState

    fun setUsername(value: String) {
        _usernameState.value = _usernameState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(TextFieldState(text = "jana@123"))
    val passwordState: State<TextFieldState> = _passwordState

    fun setPassword(value: String) {
        _passwordState.value = _passwordState.value.copy(text = value)
    }

    private val _emailState = mutableStateOf(TextFieldState(text = "jana@gmail.com"))
    val emailState: State<TextFieldState> = _emailState

    fun setEmail(value: String) {
        _emailState.value = _emailState.value.copy(text = value)
    }

    private val _rememberMeState = mutableStateOf(false)
    val rememberMeState: State<Boolean> = _rememberMeState

    fun setRememberMe(value: Boolean) {
        _rememberMeState.value = value
    }

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun loginUser() {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)

            val loginResult = loginUseCase(
                username = usernameState.value.text,
                password = passwordState.value.text,
                email = emailState.value.text,
                rememberMe = rememberMeState.value,
            )

            _loginState.value = loginState.value.copy(isLoading = false)

            if (loginResult.usernameError != null) {
                _usernameState.value = usernameState.value.copy(error = loginResult.usernameError)
            }

            if (loginResult.passwordError != null) {
                _passwordState.value = passwordState.value.copy(error = loginResult.passwordError)
            }

            if (loginResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = loginResult.emailError)
            }

            when (loginResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvents.NavigateEvent("home"))
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            loginResult.result.message ?: "Unknown error occurred!"
                        )
                    )
                }
                else -> {}
            }
        }
    }
}
