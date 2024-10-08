package com.example.mobile_application.feature_orders.data.remote

import com.example.mobile_application.feature_orders.data.remote.dto.OrderDto
import com.example.mobile_application.feature_orders.data.remote.request.CreateOrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {

    @POST("order/create")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: CreateOrderRequest
    ): Response<Unit>

    @PUT("order/status/{orderId}")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: String,
        @Body status: String
    )

    @PUT("order/cancel/{orderId}")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: String,
        @Body cancellationNote: String
    )

    @PUT("order/deliver/{orderId}")
    suspend fun markOrderDelivered(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: String,
        @Query("vendorEmail") vendorEmail: String
    )

    @GET("order/customer-orders")
    suspend fun getMyOrders(
        @Header("Authorization") token: String,
    ): List<OrderDto> // List of vendor orders

    @GET("order/all")
    suspend fun getAllOrders(): List<OrderDto> // For admin to get all orders

    @POST("orders/{orderId}/review")
    suspend fun addReview(
        @Path("orderId") orderId: String,
        @Body rating: Float,
        @Body review: String
    )
}
