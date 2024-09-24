package com.example.mobile_application.feature_products.presentation.product_details

import androidx.lifecycle.ViewModel
import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.domain.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductsRepository// Your repository
) : ViewModel() {

    suspend fun getProductById(productId: Int): Product {
        return productRepository.getProductById(productId) // Make sure this returns a Product
    }
}
