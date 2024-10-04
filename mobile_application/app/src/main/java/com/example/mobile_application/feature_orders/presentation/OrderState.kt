package com.example.mobile_application.feature_orders.presentation

import com.example.mobile_application.feature_orders.domain.model.Order

data class OrderState(
    val orders: List<Order> = emptyList(),   // List of orders
    val isLoading: Boolean = false,          // Whether the data is still loading
    val error: String? = null                // Error message in case something goes wrong
)
