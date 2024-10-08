package com.example.mobile_application.feature_orders.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    // Fetch all orders (for admin or specific users)
    suspend fun getAllOrders(): Flow<Resource<List<Order>>>

    // Fetch specific vendor's orders
    suspend fun getMyOrders(): Flow<Resource<List<Order>>>

    // Create a new order
    suspend fun createOrder(order: Order): Flow<Resource<Unit>>

    // Mark an order as delivered, possibly by a vendor
    suspend fun markOrderAsDelivered(orderId: String, vendorEmail: String?): Flow<Resource<Unit>>

    // Update the status of an order
    suspend fun updateOrderStatus(orderId: String, status: String): Flow<Resource<Unit>>

    // Cancel an order (can be by customer, admin, or CSR)
    suspend fun cancelOrder(orderId: String, note: String): Flow<Resource<Unit>>

    // Add a review to a specific order
    suspend fun addOrderReview(orderId: Int, rating: Float, review: String): Flow<Resource<Unit>>
}
