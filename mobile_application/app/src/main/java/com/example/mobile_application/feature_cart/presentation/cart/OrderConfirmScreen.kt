package com.example.mobile_application.feature_cart.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val confirmState by viewModel.confirmState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Launch effect to collect UI events like showing a Snackbar
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            ConfirmScreenContent(
                state = confirmState,
                onConfirmOrder = {
                    //viewModel.onConfirmOrder() // Implement order confirmation logic
                }
            )
        }
    }
}

@Composable
fun ConfirmScreenContent(
    state: ConfirmState,
    onConfirmOrder: () -> Unit
) {
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
            items(state.selectedItems) { item ->
                ConfirmItemRow(item = item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirmOrder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Confirm Order", fontSize = 16.sp)
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
                text = "${item.selectedQuantity} x ${item.product.price}",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
        }

        Text(
            text = "$${item.selectedQuantity * item.product.price}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
