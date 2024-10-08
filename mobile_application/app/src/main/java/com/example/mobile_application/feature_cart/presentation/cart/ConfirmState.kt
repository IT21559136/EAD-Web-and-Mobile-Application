package com.example.mobile_application.feature_cart.presentation.cart

import com.example.mobile_application.feature_cart.domain.model.CartProduct

data class ConfirmState(
    val isLoading: Boolean = false, // Indicates whether the confirmation is in progress
    val error: String? = null,       // Holds any error message
    val orderConfirmed: Boolean = false, // Indicates if the order has been confirmed
    val selectedItems: List<CartProduct> = emptyList(), // Holds the list of items being confirmed
    val totalPrice: Double = 0.0,    // Total price of the order
    val shippingAddress: String = "", // Shipping address for the order
)
