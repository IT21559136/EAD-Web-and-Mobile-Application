package com.example.mobile_application.feature_orders.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.core.domain.model.TextFieldState
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_orders.data.remote.request.CreateOrderRequest
import com.example.mobile_application.feature_orders.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val orderStatusUseCase: OrderStatusUseCase,
    private val orderReviewUseCase: OrderReviewUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
) : ViewModel() {

    // State for orders
    private val _orderState = MutableStateFlow(OrderState())
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    private val _noteState = mutableStateOf(TextFieldState())
    val noteState: State<TextFieldState> = _noteState

    fun setNote(value: String) {
        _noteState.value = _noteState.value.copy(text = value)
    }

    // Shared Flow for emitting events
    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    init {
        getMyOrders() // Fetch orders initially
    }

    // Function to create an order
    fun createOrder(orderRequest: CreateOrderRequest) {
        viewModelScope.launch {
            createOrderUseCase(orderRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent("Order created successfully!"))
                        getMyOrders() // Refresh the orders after creation
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to create order."))
                    }
                    is Resource.Loading -> {
                        _orderState.value = orderState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    // Fetch my orders
    private fun getMyOrders() {
        viewModelScope.launch {
            getOrdersUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _orderState.value = orderState.value.copy(
                            orders = result.data ?: emptyList()
                        )
                    }
                    is Resource.Loading -> {
                        _orderState.value = orderState.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to load orders."))
                    }
                }
            }
        }
    }

    // Fetch all orders
    private fun getOrders() {
        viewModelScope.launch {
            getOrdersUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _orderState.value = orderState.value.copy(
                            orders = result.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to load orders."))
                    }
                    is Resource.Loading -> {
                        // Optional: Handle loading state
                    }
                }
            }
        }
    }

    // Function to mark order as delivered
    fun markOrderAsDelivered(orderId: String, vendorEmail: String) {
        viewModelScope.launch {
            orderStatusUseCase(orderId, vendorEmail).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent("Order marked as delivered!"))
                        getOrders() // Refresh orders after marking as delivered
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to mark order as delivered."))
                    }
                    is Resource.Loading -> {
                        // Optional: Handle loading state
                    }
                }
            }
        }
    }

    // Function to cancel order
    fun cancelOrder(orderId: String, note: String) {
        viewModelScope.launch {
            cancelOrderUseCase(orderId, note).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent("Order canceled successfully!"))
                        getOrders() // Refresh orders after cancellation
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to cancel order."))
                    }
                    is Resource.Loading -> {
                        // Optional: Handle loading state
                    }
                }
            }
        }
    }

    // Function to add review to an order
    fun addOrderReview(orderId: String, rating: Float, review: String) {
        viewModelScope.launch {
            orderReviewUseCase(orderId, rating, review).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent("Review added successfully!"))
                        getOrders() // Refresh orders after adding a review
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvents.SnackbarEvent(result.message ?: "Failed to add review."))
                    }
                    is Resource.Loading -> {
                        // Optional: Handle loading state
                    }
                }
            }
        }
    }
}
