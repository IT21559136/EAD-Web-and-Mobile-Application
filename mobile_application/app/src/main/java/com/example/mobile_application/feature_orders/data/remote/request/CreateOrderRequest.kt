package com.example.mobile_application.feature_orders.data.remote.request

import com.example.mobile_application.feature_orders.data.remote.dto.OrderItemDto

data class CreateOrderRequest(
    val items: List<OrderItemDto>,
    val customerNote: String
)
