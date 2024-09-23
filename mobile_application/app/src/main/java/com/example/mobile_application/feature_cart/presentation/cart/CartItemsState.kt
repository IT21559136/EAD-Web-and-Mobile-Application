package com.example.mobile_application.feature_cart.presentation.cart

import com.example.mobile_application.feature_cart.domain.model.CartProduct

data class CartItemsState(
    val cartItems: List<CartProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)