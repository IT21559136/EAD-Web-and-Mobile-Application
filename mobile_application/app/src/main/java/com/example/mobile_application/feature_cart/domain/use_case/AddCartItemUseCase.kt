package com.example.mobile_application.feature_cart.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.repository.CartRepository
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCartItemUseCase  @Inject constructor (
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartProduct: CartProduct): Flow<Resource<Unit>> {
        return cartRepository.addCartItem(cartProduct)
    }
}
