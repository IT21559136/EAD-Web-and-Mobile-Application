package com.example.mobile_application.feature_products.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProducts(): Flow<Resource<List<Product>>>
    suspend fun getProductCategories(): List<String>
}