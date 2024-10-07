package com.example.mobile_application.feature_auth.domain.use_case

import com.example.mobile_application.feature_auth.data.remote.request.LoginRequest
import com.example.mobile_application.feature_auth.domain.model.LoginResult
import com.example.mobile_application.feature_auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        email:String,
        rememberMe: Boolean
    ): LoginResult {
        val usernameError = if (username.isBlank()) "User name cannot be blank" else null
        val passwordError = if (password.isBlank()) "Password cannot be blank" else null
        val emailError = if (email.isBlank()) "Email cannot be blank" else null


        if (usernameError != null || passwordError!=null || emailError!=null) {
            return LoginResult(
                usernameError = usernameError,
                passwordError = passwordError,
                emailError = emailError,
                confirmPasswordError = null
            )
        }

        val loginRequest = LoginRequest(
            username = username.trim(),
            password = password.trim(),
            email = email.trim(),
        )

        return LoginResult(
            result = authRepository.login(loginRequest, rememberMe)
        )
    }
}