package com.example.mobile_application.feature_orders.domain.model

import com.example.mobile_application.feature_orders.data.remote.dto.OrderItemDto

data class Order(
    val customerId: String,
    val items: List<OrderItem>,
    val customerNote: String?
)