package com.example.mobile_application.feature_orders.domain.model
data class Order(
    val id: Int,
    val productName: String,
    val image: String,
    val price: Double,
    val quantity: Int,
    val status: String,   // Can be "Pending", "Shipped", "Delivered"
    val isReviewed: Boolean
)
