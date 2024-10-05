package com.example.mobile_application.feature_auth.data.remote

import com.example.mobile_application.feature_auth.data.dto.UserResponseDto
import com.example.mobile_application.feature_auth.data.remote.request.LoginRequest
import com.example.mobile_application.feature_auth.data.remote.request.PwdRequest
import com.example.mobile_application.feature_auth.data.remote.request.RegisterRequest
import com.example.mobile_application.feature_auth.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {

    @POST("Auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    )

    @POST("Auth/login")
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
    @GET("Auth/users")
    suspend fun getUser(@Path("id") userId: Int): UserResponseDto

    @GET("users/")
    suspend fun getAllUsers(): List<UserResponseDto>
}