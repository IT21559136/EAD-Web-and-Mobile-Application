package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Order>>> {
        return repository.getMyOrders()
    }
}
