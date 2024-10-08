package com.example.mobile_application.feature_orders.data.remote

import com.example.mobile_application.feature_orders.data.remote.dto.OrderDto
import com.example.mobile_application.feature_orders.domain.model.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {

    @POST("api/order/create")
    suspend fun createOrder(
        @Body orderDto: Order
    ): Response<Unit>

    @PUT("api/order/status/{orderId}")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: String,
        @Body status: String
    )

    @PUT("api/order/cancel/{orderId}")
    suspend fun cancelOrder(
        @Path("orderId") orderId: String,
        @Body cancellationNote: String
    )

    @PUT("api/order/deliver/{orderId}")
    suspend fun markOrderDelivered(
        @Path("orderId") orderId: String,
        @Query("vendorEmail") vendorEmail: String?
    )

    @GET("api/order/my-orders")
    suspend fun getMyOrders(): List<OrderDto> // List of vendor orders

    @GET("api/order/all")
    suspend fun getAllOrders(): List<OrderDto> // For admin to get all orders

    @POST("/orders/{orderId}/review")
    suspend fun addReview(
        @Path("orderId") orderId: Int,
        @Body rating: Float,
        @Body review: String
    )
}
