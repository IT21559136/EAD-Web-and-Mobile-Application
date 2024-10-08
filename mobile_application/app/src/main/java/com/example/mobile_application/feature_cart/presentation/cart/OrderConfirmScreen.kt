package com.example.mobile_application.feature_cart.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.util.LoadingAnimation
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_auth.data.remote.request.OrderItemRequest
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_orders.data.remote.request.CreateOrderRequest
import com.example.mobile_application.feature_orders.presentation.OrderViewModel
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {

    val state by viewModel.confirmState.collectAsState()
    val orderState by orderViewModel.orderState.collectAsState()


    val snackBarHostState = remember { SnackbarHostState() }


    // Launch effect to collect UI events like showing a Snackbar
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = Color.White, // Background color
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Order Confirmation",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainWhiteColor // Background color for top bar
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingAnimation(
                        circleSize = 16.dp,
                    )
                }
            }else if (state.error != null) {
                // Show an error message if there is an error
                Text(text = "Error: ${state.error}", color = Color.Red)
            } else {
                ConfirmScreenContent(
                    state = state,
                    selectedItems = state.selectedItems,
                    totalPrice = state.totalPrice,
                    onConfirmOrder = {
                        val orderRequest = CreateOrderRequest(
                            items = state.selectedItems.map { item ->
                                // Map each CartProduct to a request model
                                OrderItemRequest(
                                    productId = item.product.id,
                                    quantity = item.selectedQuantity
                                )
                            },
                            customerNote = state.customerNote
                        )
                        orderViewModel.createOrder(orderRequest)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreenContent(
    state: ConfirmState,
    selectedItems: List<CartProduct>,
    totalPrice: Double,
    onConfirmOrder: () -> Unit
) {
    // Log the data being passed to this Composable
    Timber.d("ConfirmScreenContent - Selected Items: $selectedItems")
    Timber.d("ConfirmScreenContent - Total Price: $totalPrice")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your Order Details",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f) // Takes up remaining space
        ) {
            items(selectedItems) { item ->
                ConfirmItemRow(item = item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total Price: $$totalPrice",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.End
        )

        // Customer note text field
        TextField(
            value = state.customerNote,
            onValueChange = { state.customerNote = it },
            label = { Text("Customer Note") },
            placeholder = { Text("Add any special instructions...") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MainWhiteColor,
                unfocusedIndicatorColor = Color.Gray,
                containerColor = Color.Transparent // Making it modern
            )
        )

        Button(
            onClick = onConfirmOrder,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Confirm Order",
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ConfirmItemRow(item: CartProduct) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = item.product.image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = item.product.productName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Total Price = ${item.selectedQuantity} x ${item.product.price}",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )

            Text(
                text = "$${item.selectedQuantity * item.product.price}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }


    }
}
