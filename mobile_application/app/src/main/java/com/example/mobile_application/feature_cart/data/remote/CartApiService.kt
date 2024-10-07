package com.example.mobile_application.feature_cart.data.remote

import com.example.mobile_application.feature_cart.data.remote.dto.UserCartDto
import com.example.mobile_application.feature_cart.data.remote.request.AddCartItemRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService {

    // Get the cart items for the authenticated user (user ID from token)
    @GET("Cart")
    suspend fun getUserCart(
        @Header("Authorization") token: String,
    ): UserCartDto

    // Add or update an item in the cart
    @POST("cart/add-item")
    suspend fun addOrUpdateCartItem(
        @Header("Authorization") token: String,
        @Body cartItemDto: AddCartItemRequest
    ): UserCartDto

    // Remove an item from the cart
    @DELETE("cart/remove-item/{productId}")
    suspend fun removeCartItem(
        @Header("Authorization") token: String,
        @Path("productId") productId: String
    ): Unit
}
