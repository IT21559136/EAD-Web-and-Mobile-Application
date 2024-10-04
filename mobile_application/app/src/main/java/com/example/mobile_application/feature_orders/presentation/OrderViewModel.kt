package com.example.mobile_application.feature_orders.presentation


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_orders.domain.use_case.GetOrdersUseCase
import com.example.mobile_application.feature_orders.domain.use_case.OrderStatusUseCase
import com.example.mobile_application.feature_orders.domain.use_case.OrderReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val orderStatusUseCase: OrderStatusUseCase,
    private val orderReviewUseCase: OrderReviewUseCase
) : ViewModel() {
    private val _orderState = mutableStateOf(OrderState())
    val orderState: State<OrderState> = _orderState

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            getOrdersUseCase().collect { result ->
                _orderState.value = orderState.value.copy(
                    orders = result
                )
            }
        }
    }

    fun markOrderAsDelivered(orderId: Int) {
        viewModelScope.launch {
            orderStatusUseCase(orderId)
        }
    }

    fun addOrderReview(orderId: Int, rating: Float, review: String) {
        viewModelScope.launch {
            orderReviewUseCase(orderId, rating, review)
        }
    }
}
