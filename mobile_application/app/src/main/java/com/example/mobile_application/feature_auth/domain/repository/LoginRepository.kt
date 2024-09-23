package com.example.mobile_application.feature_auth.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.data.remote.request.LoginRequest

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest, rememberMe: Boolean): Resource<Unit>
    suspend fun autoLogin(): Resource<Unit>
    suspend fun logout(): Resource<Unit>
}
