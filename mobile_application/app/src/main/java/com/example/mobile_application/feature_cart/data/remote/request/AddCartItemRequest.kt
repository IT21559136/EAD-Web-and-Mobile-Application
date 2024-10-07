package com.example.mobile_application.feature_cart.data.remote.request

import com.google.gson.annotations.SerializedName

data class AddCartItemRequest (
    @SerializedName("productId")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)