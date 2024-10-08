package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, note: String): Flow<Resource<Unit>> {
        return orderRepository.cancelOrder(orderId, note)
    }
}
