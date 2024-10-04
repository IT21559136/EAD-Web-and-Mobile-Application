package com.example.mobile_application.feature_orders.domain.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getAllOrders(): Flow<Resource<List<Order>>>
    suspend fun markOrderAsDelivered(orderId: Int)
    suspend fun addOrderReview(orderId: Int, rating: Float, review: String)
}
