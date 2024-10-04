package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class OrderStatusUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int) {
        repository.markOrderAsDelivered(orderId)
    }
}
