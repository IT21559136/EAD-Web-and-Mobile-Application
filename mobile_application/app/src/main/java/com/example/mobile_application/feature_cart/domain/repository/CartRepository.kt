package com.example.mobile_application.feature_cart.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCartItems(id: Int): Flow<Resource<List<CartProduct>>>
}