package com.example.mobile_application.feature_cart.domain.model

data class UserCart(
    //val id: Int,
    val customerId: String,
    val cartProducts: List<CartProduct>,
    val totalPrice: Double
)
