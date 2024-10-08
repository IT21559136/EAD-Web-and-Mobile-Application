package com.example.mobile_application.feature_orders.data.remote.request

import com.example.mobile_application.feature_auth.data.remote.request.OrderItemRequest

data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    val customerNote: String
)
