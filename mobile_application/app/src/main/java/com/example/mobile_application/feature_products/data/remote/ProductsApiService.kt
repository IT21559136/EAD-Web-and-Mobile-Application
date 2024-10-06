package com.example.mobile_application.feature_products.data.remote

import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import com.example.mobile_application.feature_products.domain.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsApiService {

    @GET("Product/list")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/categories")
    suspend fun getProductCategories(): List<String>
    @GET("Product/{id}") // New method to get a product by ID
    suspend fun getProductById(@Path("id") productId: Int): Product
}