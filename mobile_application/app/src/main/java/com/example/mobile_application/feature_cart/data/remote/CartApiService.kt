package com.example.mobile_application.feature_cart.data.remote

import com.example.mobile_application.feature_cart.data.remote.dto.UserCartResponseDto
import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CartApiService {
    @GET("carts/user/{id}")
    suspend fun cartItems(
        @Path("id") id: Int
    ): List<UserCartResponseDto>

    @GET("products/{id}")
    suspend fun product(
        @Path("id") id: Int
    ): ProductDto
}