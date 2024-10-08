package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.data.remote.request.CreateOrderRequest
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderRequest: CreateOrderRequest): Flow<Resource<Unit>> {
        return orderRepository.createOrder(orderRequest)
    }
}
