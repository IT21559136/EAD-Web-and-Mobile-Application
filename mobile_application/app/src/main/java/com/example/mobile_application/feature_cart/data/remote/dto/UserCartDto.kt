package com.example.mobile_application.feature_cart.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserCartDto(
//    @SerializedName("id")
//    val id: Int,
    @SerializedName("customerId")
    val customerId: String,
    @SerializedName("items")
    val items: List<CartProductDto>,
    @SerializedName("totalPrice")
    val totalPrice: Double,
)