package com.example.mobile_application.feature_cart.domain.use_case

import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    // This method will delete the provided list of cart items
    suspend operator fun invoke(cartItems: List<CartProduct>) {
        repository.deleteCartItems(cartItems)
    }
}
