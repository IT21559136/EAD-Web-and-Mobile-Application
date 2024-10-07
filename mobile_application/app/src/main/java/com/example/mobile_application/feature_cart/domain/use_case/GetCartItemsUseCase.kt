package com.example.mobile_application.feature_cart.domain.use_case

import com.google.gson.Gson
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemsUseCase(
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke(): Flow<Resource<List<CartProduct>>> {
        return cartRepository.getAllCartItems()
    }
}