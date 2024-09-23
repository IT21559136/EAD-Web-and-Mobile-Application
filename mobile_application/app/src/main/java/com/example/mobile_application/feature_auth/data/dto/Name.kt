package com.example.mobile_application.feature_auth.data.dto


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String
)