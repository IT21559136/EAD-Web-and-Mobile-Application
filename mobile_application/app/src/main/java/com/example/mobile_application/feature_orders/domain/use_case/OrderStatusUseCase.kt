package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderStatusUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, vendorEmail:String?):Flow<Resource<Unit>> {
        return repository.markOrderAsDelivered(orderId, vendorEmail )
    }
}
