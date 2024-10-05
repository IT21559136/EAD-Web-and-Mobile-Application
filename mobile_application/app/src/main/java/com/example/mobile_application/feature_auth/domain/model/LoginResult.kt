package com.example.mobile_application.feature_auth.domain.model

import com.example.mobile_application.core.util.Resource

data class LoginResult(
    val passwordError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val confirmPasswordError: String?=null,
    val result: Resource<Unit>? = null
)
