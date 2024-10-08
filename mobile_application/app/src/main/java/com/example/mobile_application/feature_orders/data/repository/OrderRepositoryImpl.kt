package com.example.mobile_application.feature_orders.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.data.remote.OrderApiService
import com.example.mobile_application.feature_orders.data.remote.mappers.toDomain
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
) : OrderRepository {

    override suspend fun createOrder(order: Order): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderApiService.createOrder(order)

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))  // The request was successful
            } else {
                emit(Resource.Error(message = "Failed to create order. Please try again later."))
            }
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Server error occurred. Please try again later."))
        }
    }

    override suspend fun getAllOrders(): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderApiService.getAllOrders()
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun getMyOrders(): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderApiService.getMyOrders()
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun markOrderAsDelivered(orderId: String, vendorEmail: String?): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            orderApiService.markOrderDelivered(orderId, vendorEmail)
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            orderApiService.updateOrderStatus(orderId, status)
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun cancelOrder(orderId: String, note: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            orderApiService.cancelOrder(orderId, note)
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun addOrderReview(orderId: Int, rating: Float, review: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            orderApiService.addReview(orderId, rating, review)
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Failed to submit review. Please try again!"))
        }
    }
}
