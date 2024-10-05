package com.example.mobile_application.feature_auth.data.remote.request

import com.google.gson.annotations.SerializedName

data class PwdRequest (
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)

