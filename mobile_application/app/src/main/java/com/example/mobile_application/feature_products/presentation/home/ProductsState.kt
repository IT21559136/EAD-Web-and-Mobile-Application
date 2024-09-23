package com.example.mobile_application.feature_products.presentation.home

import com.example.mobile_application.feature_products.domain.model.Product

data class ProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)