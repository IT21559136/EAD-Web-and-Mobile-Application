package com.example.mobile_application.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String
)