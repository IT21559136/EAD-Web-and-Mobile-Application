package com.example.mobile_application.feature_orders.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.data.remote.OrderApiService
import com.example.mobile_application.feature_orders.data.remote.mappers.toDomain
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import com.example.mobile_application.feature_products.data.remote.mappers.toDomain
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService // Assuming you have an API service
) : OrderRepository {

    override suspend fun getAllOrders(): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderApiService.getOrders()
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        }
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
