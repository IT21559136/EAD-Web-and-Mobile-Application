package com.example.mobile_application.feature_auth.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.core.domain.model.TextFieldState
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_auth.domain.use_case.RegisterUseCase
import com.example.mobile_application.feature_auth.presentation.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _usernameState = mutableStateOf(TextFieldState())
    val usernameState: State<TextFieldState> = _usernameState

    fun setUsername(value: String) {
        _usernameState.value = _usernameState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(TextFieldState())
    val passwordState: State<TextFieldState> = _passwordState

    fun setPassword(value: String) {
        _passwordState.value = _passwordState.value.copy(text = value)
    }

    private val _emailState = mutableStateOf(TextFieldState())
    val emailState: State<TextFieldState> = _emailState

    fun setEmail(value: String) {
        _emailState.value = _emailState.value.copy(text = value)
    }

    private val _confirmPwdState = mutableStateOf(TextFieldState())
    val confirmPwdState: State<TextFieldState> = _confirmPwdState

    fun setConfirmPassword(value: String) {
        _confirmPwdState.value = _confirmPwdState.value.copy(text = value)
    }

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    // Event flow to notify UI about events (like success or errors)
    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    fun registerUser() {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)

            val registerResult = registerUseCase(
                username = usernameState.value.text,
                password = passwordState.value.text,
                confirmPassword = confirmPwdState.value.text,
                email = emailState.value.text
            )

            _loginState.value = loginState.value.copy(isLoading = false)

            if (registerResult.usernameError != null) {
                _usernameState.value = usernameState.value.copy(error = registerResult.usernameError)
            }

            if (registerResult.passwordError != null) {
                _passwordState.value = passwordState.value.copy(error = registerResult.passwordError)
            }

            if (registerResult.confirmPasswordError != null) {
                _confirmPwdState.value = confirmPwdState.value.copy(error = registerResult.confirmPasswordError)
            }

            if (registerResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = registerResult.emailError)
            }

            when (registerResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvents.SnackbarEvent("Registration successful!\nYour account will be activated soon..."))
                    //_eventFlow.emit(UiEvents.NavigateEvent("login"))
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            registerResult.result.message ?: "Unknown error occurred!"
                        )
                    )
                }
                else -> {}
            }
        }
    }
}
