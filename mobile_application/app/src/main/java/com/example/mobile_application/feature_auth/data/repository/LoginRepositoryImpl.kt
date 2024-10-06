package com.example.mobile_application.feature_auth.data.repository

import android.util.Log
import com.example.mobile_application.core.util.JwtUtils.getUserIdFromToken
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.data.dto.UserResponseDto
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_auth.data.remote.AuthApiService
import com.example.mobile_application.feature_auth.data.remote.request.LoginRequest
import com.example.mobile_application.feature_auth.data.remote.request.RegisterRequest
import com.example.mobile_application.feature_auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class LoginRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) : LoginRepository {
    override suspend fun register(registerRequest: RegisterRequest): Resource<Unit> {
        Timber.d("Register called")
        return try {
            // Send the registration request to the API
            authApiService.registerUser(registerRequest)

            // Return success if no issues
            Resource.Success(Unit)
        } catch (e: IOException) {
            Timber.e("Registration failed due to network error: ${e.localizedMessage}")
            Resource.Error(message = "Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            Timber.e("Registration failed due to server error: ${e.localizedMessage}")
            Resource.Error(message = "An unknown error occurred, please try again!")
        }
    }

    override suspend fun login(loginRequest: LoginRequest, rememberMe: Boolean): Resource<Unit> {
        Timber.d("Login called")
        return try {
            val response = authApiService.loginUser(loginRequest)
            Timber.d("Login Token: ${response.token},  Role: ${response.role}")
            authPreferences.saveAccessToken(response.token)
            Timber.d("Auth Preferences Login Token",authPreferences.getAccessToken)
//            if (rememberMe) {
//                authPreferences.saveAccessToken(response.token)
//            }

            // Extract user ID from the token
            val userId = getUserIdFromToken(response.token)
            Timber.d("Extracted User ID: $userId")

//            if (userId != null) {
//                //fetch more user data using the userId
//                val bearerToken = "Bearer ${response.token}"
//                val userResponse = authApiService.getUser(bearerToken, userId)
//                authPreferences.saveUserdata(userResponse)
//            }
           // getAllUsers(loginRequest.username)?.let { authPreferences.saveUserdata(it) }

            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.d("login error","Login failed due to network error: ${e.localizedMessage}")
            Timber.e("Login failed due to network error: ${e.localizedMessage}")
            Resource.Error(message = "Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            Timber.e("Login failed due to server error: ${e.localizedMessage}")
            Resource.Error(message = "An Unknown error occurred, please try again!")
        }
    }

    override suspend fun autoLogin(): Resource<Unit> {
        val accessToken = authPreferences.getAccessToken.first()
        Timber.d("Auto login access token: $accessToken")
        return if (accessToken != "") {
            Resource.Success(Unit)
        } else {
            Resource.Error("")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            authPreferences.clearAccessToken()
            val fetchedToken = authPreferences.getAccessToken.first()
            Timber.d("token: $fetchedToken")

            if (fetchedToken.isEmpty()) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Unknown Error")
            }
        } catch (e: Exception) {
            return Resource.Error("Unknown error occurred")
        }
    }

    private suspend fun getAllUsers(name: String): UserResponseDto? {
        val response = authApiService.getAllUsers()
        return response.find { it.username == name }
    }
}