package com.example.mobile_application.feature_orders.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.mobile_application.core.presentation.ui.theme.CancelColor
import com.example.mobile_application.core.presentation.ui.theme.DarkBlue
import com.example.mobile_application.core.presentation.ui.theme.DeliveredStatusColor
import com.example.mobile_application.core.presentation.ui.theme.EditColor
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.presentation.ui.theme.PartialStatusColor
import com.example.mobile_application.core.presentation.ui.theme.ProcessingStatusColor
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_orders.domain.model.Order
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    val state by viewModel.orderState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    // Collect events like success or failure messages
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackBarHostState.showSnackbar(event.message)
                }
                is UiEvents.NavigateEvent -> {
                    navController.navigate(event.route)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainWhiteColor//Color.White
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "My Orders",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OrderScreenContent(
                orders = state.orders,
                onMarkDelivered = { orderId,vendorEmail -> viewModel.markOrderAsDelivered(orderId, vendorEmail) },
                onRateOrder = { orderId, rating, review -> viewModel.addOrderReview(orderId, rating, review) },
                modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp),
            )
        }
    }
}

@Composable
fun OrderScreenContent(
    orders: List<Order>,
    onMarkDelivered: (String, String) -> Unit,
    onRateOrder: (String, Float, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier.fillMaxSize().padding(horizontal = 12.dp)
    ) {
        items(orders) { order ->
            OrderItem(
                order = order,
                onMarkDelivered = onMarkDelivered,
                onRateOrder = onRateOrder,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    onMarkDelivered: (String, String) -> Unit,
    onRateOrder: (String, Float, String) -> Unit,
//    onOrderDetailsClick: (Order) -> Unit, // Callback for navigating to order details page
//    onCancelOrder: (String) -> Unit, // Callback for canceling the order
//    onEditOrder: (String) -> Unit // Callback for editing the order
) {
    var rating by remember { mutableStateOf(0f) }
    var review by remember { mutableStateOf("") }
    var showReviewDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { /*onOrderDetailsClick(order)*/ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order #${order.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Iterate over each item in the order and display its product name
            order.items.forEach { item ->
                Text(
                    text = item.productName, // Assuming OrderItem has a property 'productName'
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp)) // Add some space between item names
            }

            OutlinedButton(
                onClick = { /* No-op for non-clickable button */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = when (order.status) {
                        "Partially Delivered" -> PartialStatusColor.copy(alpha = 0.2f)
                        "Delivered" -> DeliveredStatusColor.copy(alpha = 0.2f)
                        "Processing" -> ProcessingStatusColor.copy(alpha = 0.2f)
                        else -> Color.Red.copy(alpha = 0.2f)
                    },
                ),
                border = BorderStroke(2.dp, when (order.status) {
                    "Partially Delivered" -> PartialStatusColor.copy(alpha = 0.7f)
                    "Delivered" -> DeliveredStatusColor.copy(alpha = 0.7f)
                    "Processing" -> ProcessingStatusColor.copy(alpha = 0.7f)
                    else -> Color.Red.copy(alpha = 0.7f)
                }),
                //enabled = false // Non-clickable status button
            ) {
                Text(text = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Show rating if order is delivered
                if (order.status == "Delivered") {
                    Button(
                        onClick = { showReviewDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = YellowMain
                        )
                    ) {
                        Text(text = "Rate Vendor")
                    }
                }

//                if (order.status == "Delivered" && !order.isReviewed) {
//                    Button(onClick = { showReviewDialog = true }) {
//                        Text(text = "Rate Order")
//                    }
//                }

                // Show "Cancel" and "Edit" buttons if order status is "Processing"
                if (order.status == "Processing") {
                    Row(
                        modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
                        horizontalArrangement = Arrangement.End, // Aligns items to the end
                        verticalAlignment = Alignment.CenterVertically, // Optionally align vertically
                    ) {
                        Button(
                            onClick = { /*onCancelOrder(order.id)*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CancelColor
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { /*onEditOrder(order.id)*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EditColor
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(text = "Edit")
                        }
                    }

                }

            }

                // Review dialog
            if (showReviewDialog) {
                AlertDialog(
                    onDismissRequest = { showReviewDialog = false },
                    title = { Text(text = "Rate Order") },
                    text = {
                        Column {
                            RatingBar(rating = rating, onRatingChange = { rating = it })
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = review,
                                onValueChange = { review = it },
                                label = { Text(text = "Review") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } },
                    confirmButton = {
                        Button(onClick = {
                            onRateOrder(order.id, rating, review)
                            showReviewDialog = false
                        }) {
                            Text(text = "Submit")
                        } },
                    dismissButton = {
                        Button(onClick = { showReviewDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun RatingBar(rating: Float, onRatingChange: (Float) -> Unit) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= rating) YellowMain else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRatingChange(i.toFloat()) }
            )
        }
    }
}
