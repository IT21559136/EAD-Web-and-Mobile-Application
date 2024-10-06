package com.example.mobile_application.feature_auth.data.remote

import com.example.mobile_application.feature_auth.data.dto.UserResponseDto
import com.example.mobile_application.feature_auth.data.remote.request.LoginRequest
import com.example.mobile_application.feature_auth.data.remote.request.PwdRequest
import com.example.mobile_application.feature_auth.data.remote.request.RegisterRequest
import com.example.mobile_application.feature_auth.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {

    @POST("Auth/register")
    @Headers("Content-Type: text/plain")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<Unit>

    @POST("Auth/login")
    @Headers("Content-Type: application/json")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("Auth/deactivate")
    suspend fun deactivateUser()

    @POST("Auth/change-password")
    suspend fun changePassword(
        @Body pwdRequest: PwdRequest
    )
    @DELETE("Auth/delete-account")
    suspend fun deleteAccount()
    @GET("Auth/users/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") userId: String?
    ): UserResponseDto

    @GET("users/")
    suspend fun getAllUsers(): List<UserResponseDto>
}