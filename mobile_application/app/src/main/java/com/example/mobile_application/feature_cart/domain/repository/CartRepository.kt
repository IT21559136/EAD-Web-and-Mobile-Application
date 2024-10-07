package com.example.mobile_application.feature_cart.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCartItems(): Flow<Resource<List<CartProduct>>>
    suspend fun deleteCartItems(cartItems: List<CartProduct>)
    suspend fun addCartItem(cartProduct: CartProduct): Flow<Resource<Unit>>
}