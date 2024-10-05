package com.example.mobile_application.feature_auth.domain.use_case

import com.example.mobile_application.feature_auth.data.remote.request.RegisterRequest
import com.example.mobile_application.feature_auth.domain.model.LoginResult
import com.example.mobile_application.feature_auth.domain.repository.LoginRepository

class RegisterUseCase (
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke(
        username: String,
        password: String,
        confirmPassword:String,
        email:String
    ): LoginResult {
        val usernameError = if (username.isBlank()) "User name cannot be blank" else null
        val passwordError = if (password.isBlank()) "Password cannot be blank" else null
        val emailError = if (email.isBlank()) "Email cannot be blank" else null
        //val confirmPasswordError = if (password != confirmPassword)  "Passwords do not match" else null

        val confirmPasswordError = if (confirmPassword.isBlank()) "Password cannot be blank"
                                   else if (password != confirmPassword) "Passwords do not match"
                                   else null

        if (usernameError != null || passwordError!=null || emailError!=null || confirmPasswordError!=null) {
            return LoginResult(
                usernameError = usernameError,
                passwordError = passwordError,
                emailError = emailError,
                confirmPasswordError = confirmPasswordError
            )
        }

        val registerRequest = RegisterRequest(
            username = username.trim(),
            password = password.trim(),
            email = email.trim(),
        )

        return LoginResult(
            result = loginRepository.register(registerRequest)
        )
    }
}