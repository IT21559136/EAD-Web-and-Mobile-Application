package com.example.mobile_application.feature_orders.data.remote.dto

import com.google.gson.annotations.SerializedName
data class OrderItemDto(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("vendorEmail")
    val vendorEmail: String
)
