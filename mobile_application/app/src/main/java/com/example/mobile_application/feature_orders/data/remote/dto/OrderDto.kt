package com.example.mobile_application.feature_orders.data.remote.dto

import com.example.mobile_application.feature_products.data.remote.dto.RatingDto
import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("isReviewed")
    val isReviewed: Boolean
)