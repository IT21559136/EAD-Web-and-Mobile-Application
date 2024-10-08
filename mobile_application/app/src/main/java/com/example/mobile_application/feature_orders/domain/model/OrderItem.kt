package com.example.mobile_application.feature_orders.domain.model
data class OrderItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val vendorEmail: String,
    val orderItemStatus: String
)