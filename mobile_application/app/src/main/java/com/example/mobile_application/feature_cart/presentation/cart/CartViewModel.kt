package com.example.mobile_application.feature_cart.presentation.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.use_case.AddCartItemUseCase
import com.example.mobile_application.feature_cart.domain.use_case.DeleteCartItemsUseCase
import com.example.mobile_application.feature_cart.domain.use_case.GetCartItemsUseCase
import com.example.mobile_application.feature_products.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase,
    private val addCartItemUseCase: AddCartItemUseCase
) : ViewModel() {
    private val _state = mutableStateOf(CartItemsState())
    val state: State<CartItemsState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            getCartItems()
        }
    }

    private suspend fun getCartItems() {
        getCartItemsUseCase().collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        cartItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "Unknown error occurred!"
                        )
                    )
                }
            }
        }
    }

    // Add a new item to the cart
    fun addCartItem(product: Product, selectedQuantity: Int) {
        val cartProduct = CartProduct(product,  selectedQuantity) // Create a CartProduct instance
        viewModelScope.launch {
            addCartItemUseCase(cartProduct).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Update the state with the new cart item if needed
                        _state.value = state.value.copy(
                            cartItems = state.value.cartItems + cartProduct // Add the item to the cart
                        )
                        // Show a snackbar confirming the addition
                        _eventFlow.emit(
                            UiEvents.SnackbarEvent(
                                message = "${cartProduct.product.productName} has been added to the cart."
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(
                            UiEvents.SnackbarEvent(
                                message = result.message ?: "Failed to add item to the cart."
                            )
                        )
                    }
                }
            }
        }
    }

    // Add or remove a cart item from the selected items list
    fun onItemSelected(item: CartProduct, isSelected: Boolean) {
        _state.value = state.value.copy(
            selectedItems = if (isSelected) {
                state.value.selectedItems + item // Add item to selectedItems
            } else {
                state.value.selectedItems - item // Remove item from selectedItems
            }
        )
    }

    // Delete all selected items from the cart
    fun onDeleteSelectedItems() {
        viewModelScope.launch {
            val selectedItems = state.value.selectedItems
            if (selectedItems.isNotEmpty()) {
                val updatedCartItems = state.value.cartItems - selectedItems
                _state.value = state.value.copy(
                    cartItems = updatedCartItems,
                    selectedItems = emptyList() // Clear selected items after deletion
                )

                // Perform deletion in repository (use case)
                deleteCartItemsUseCase(selectedItems)

                // Show a snackbar confirming the deletion
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(
                        message = "Selected items have been deleted."
                    )
                )
            } else {
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(
                        message = "No items selected for deletion."
                    )
                )
            }
        }
    }
}