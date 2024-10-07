package com.example.mobile_application.feature_cart.data.remote.dto

data class CartProductDto(
    val productId: String,
    val productName: String,
    val quantity: Int, // This represents the quantity selected in the cart
    val price: Double,
    val availableQuantity: Int,
    val category: String,
    val description: String,
    val image: String
)