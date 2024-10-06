package com.example.mobile_application.feature_products.data.remote

import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import com.example.mobile_application.feature_products.domain.model.Category
import com.example.mobile_application.feature_products.domain.model.Product
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductsApiService {

    @GET("Product/all")
    suspend fun getProducts(
        @Header("Authorization") token: String,
    ): List<ProductDto>
    @GET("Category")
    suspend fun getProductCategories(
        @Header("Authorization") token: String,
    ): List<Category>

    @GET("Product/{id}") // New method to get a product by ID
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): Product
}