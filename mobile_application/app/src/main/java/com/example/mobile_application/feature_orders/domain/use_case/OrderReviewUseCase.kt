package com.example.mobile_application.feature_orders.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderReviewUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int, rating: Float, review: String): Flow<Resource<Unit>> = flow  {
        repository.addOrderReview(orderId, rating, review)
    }
}
