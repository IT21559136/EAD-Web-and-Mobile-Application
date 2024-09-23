package com.example.mobile_application.feature_cart.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CartProductDto(
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int
)