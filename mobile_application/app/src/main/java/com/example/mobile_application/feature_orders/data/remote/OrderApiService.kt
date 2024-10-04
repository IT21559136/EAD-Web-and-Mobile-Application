package com.example.mobile_application.feature_orders.data.remote

import com.example.mobile_application.feature_orders.domain.model.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @GET("/orders")
    suspend fun getOrders(): List<Order>  // Replace with actual response model

    @POST("/orders/{orderId}/deliver")
    suspend fun markOrderDelivered(@Path("orderId") orderId: Int)

    @POST("/orders/{orderId}/review")
    suspend fun addReview(
        @Path("orderId") orderId: Int,
        @Body rating: Float,
        @Body review: String
    )
}
