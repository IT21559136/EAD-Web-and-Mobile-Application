package com.example.mobile_application.feature_orders.data.repository

import com.example.mobile_application.feature_orders.data.remote.OrderApiService
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService // Assuming you have an API service
) : OrderRepository {

    override suspend fun getAllOrders(): Flow<List<Order>> = flow {
        // Fetch orders from the API or database
        val response = orderApiService.getOrders()  // Replace with actual API call
        emit(response)
    }

    override suspend fun markOrderAsDelivered(orderId: Int) {
        // Update order status in API/database
        orderApiService.markOrderDelivered(orderId)  // Replace with actual API call
    }

    override suspend fun addOrderReview(orderId: Int, rating: Float, review: String) {
        // Submit the review and rating to the API/database
        orderApiService.addReview(orderId, rating, review)  // Replace with actual API call
    }
}
