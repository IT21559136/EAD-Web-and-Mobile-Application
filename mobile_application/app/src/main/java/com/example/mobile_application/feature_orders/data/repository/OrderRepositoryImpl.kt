package com.example.mobile_application.feature_orders.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_orders.data.remote.OrderApiService
import com.example.mobile_application.feature_orders.data.remote.mappers.toDomain
import com.example.mobile_application.feature_orders.data.remote.request.CreateOrderRequest
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService,
    private val authPreferences: AuthPreferences
) : OrderRepository {

    private val accessToken: String by lazy {
        runBlocking { authPreferences.getAccessToken.first() }
    }
    private val bearerToken = "Bearer $accessToken"


    override suspend fun createOrder(orderRequest: CreateOrderRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderApiService.createOrder(bearerToken, orderRequest)

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

//    override suspend fun getAllOrders(): Flow<Resource<List<Order>>> = flow {
//        emit(Resource.Loading())
//        try {
//            val response = orderApiService.getAllOrders()
//            emit(Resource.Success(response.map { it.toDomain() }))
//        } catch (e: IOException) {
//            Timber.e("Network error fetching all orders: ${e.localizedMessage}")
//            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
//        } catch (e: HttpException) {
//            Timber.e("HTTP error fetching all orders: ${e.message()} (Code: ${e.code()})")
//            emit(Resource.Error("An unknown error occurred."))
//        }
//    }

    override suspend fun getMyOrders(): Flow<Resource<List<Order>>> = flow {
        Timber.d("Get My Orders called")
        emit(Resource.Loading())
        try {
            val response = orderApiService.getMyOrders(bearerToken)
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            Timber.e("Network error fetching my orders: ${e.localizedMessage}")
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            Timber.e("HTTP error fetching my orders: ${e.message()} (Code: ${e.code()})")
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun markOrderAsDelivered(orderId: String, vendorEmail: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            orderApiService.markOrderDelivered(bearerToken,orderId, vendorEmail)
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
            orderApiService.updateOrderStatus(bearerToken,orderId, status)
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
            orderApiService.cancelOrder(bearerToken,orderId, note)
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error("Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error("An unknown error occurred."))
        }
    }

    override suspend fun addOrderReview(orderId: String, rating: Float, review: String): Flow<Resource<Unit>> = flow {
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
