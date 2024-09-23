package com.example.mobile_application.feature_products.domain.use_case

import com.example.mobile_application.feature_products.domain.repository.ProductsRepository
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Product>>> {
        return productsRepository.getProducts()
    }
}