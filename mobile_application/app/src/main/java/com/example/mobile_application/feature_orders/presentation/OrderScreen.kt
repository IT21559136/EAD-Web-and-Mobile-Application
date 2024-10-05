package com.example.mobile_application.feature_orders.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mobile_application.R
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
    val state = viewModel.orderState.value
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect events like success or failure messages
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(event.message)
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
                title = { Text(text = "Orders", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OrderScreenContent(
                orders = state.orders,
                onMarkDelivered = { orderId -> viewModel.markOrderAsDelivered(orderId) },
                onRateOrder = { orderId, rating, review -> viewModel.addOrderReview(orderId, rating, review) }
            )
        }
    }
}

@Composable
fun OrderScreenContent(
    orders: List<Order>,
    onMarkDelivered: (Int) -> Unit,
    onRateOrder: (Int, Float, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(orders) { order ->
            OrderItem(
                order = order,
                onMarkDelivered = onMarkDelivered,
                onRateOrder = onRateOrder
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    onMarkDelivered: (Int) -> Unit,
    onRateOrder: (Int, Float, String) -> Unit
) {
    var rating by remember { mutableStateOf(0f) }
    var review by remember { mutableStateOf("") }
    var showReviewDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
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
                Text(
                    text = order.status,
                    color = when (order.status) {
                        "Pending" -> Color.Gray
                        "Shipped" -> Color.Blue
                        "Delivered" -> Color.Green
                        else -> Color.Red
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(order.image)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.productName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Show "Mark as Delivered" button if order is shipped
                if (order.status == "Shipped") {
                    Button(onClick = { onMarkDelivered(order.id) }) {
                        Text(text = "Mark as Delivered")
                    }
                }

                // Show rating if order is delivered
                if (order.status == "Delivered" && !order.isReviewed) {
                    Button(onClick = { showReviewDialog = true }) {
                        Text(text = "Rate Order")
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
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                onRateOrder(order.id, rating, review)
                                showReviewDialog = false
                            }) {
                                Text(text = "Submit")
                            }
                        },
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
