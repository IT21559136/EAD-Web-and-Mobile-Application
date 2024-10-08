package com.example.mobile_application.feature_cart.presentation.cart

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mobile_application.R
import com.example.mobile_application.core.presentation.ui.theme.DarkBlue
import com.example.mobile_application.core.presentation.ui.theme.GrayColor
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.core.util.LoadingAnimation
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.model.toJson
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = hiltViewModel(),
) {

    val state  by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Trigger getCartItems every time the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }

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
        containerColor = Color.White,//MainWhiteColor,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainWhiteColor//Color.White
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "My Cart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    // Add a delete button in the top bar
                    IconButton(onClick = {
                        viewModel.onDeleteSelectedItems() // Implement this in ViewModel
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Delete Selected Items",
                            tint = DarkBlue
                        )
                    }
                }
            )
        }
    ) {padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CartScreenContent(
                state = state,
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp),
                onItemSelected = { item, isSelected ->
                    viewModel.onItemSelected(item, isSelected)
                }
            )
        }
    }
}

@Composable
private fun CartScreenContent(
    state: CartItemsState,
    viewModel: CartViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemSelected: (CartProduct, Boolean) -> Unit
) {
    Box(modifier.fillMaxSize()) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 190.dp) // Leave space for the CheckoutComponent
        ) {
            items(state.cartItems) { cartItem ->
                CartItem(
                    cartItem = cartItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(4.dp),
                    isSelected = state.selectedItems.contains(cartItem), // Check if item is selected
                    onItemSelected = onItemSelected
                )
            }
        }

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
        }

        if (state.error != null) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = state.error,
                    color = Color.Red
                )
            }
        }

        if (state.cartItems.isEmpty() && state.cartItems == null) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(220.dp),
                    painter = painterResource(id = R.drawable.ic_artwork),
                    contentDescription = null
                )
                Text(
                    text = "Your cart is empty",
                    style = MaterialTheme.typography.titleMedium,
                    color = GrayColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Display the fixed CheckoutComponent at the bottom
        if (state.cartItems.isNotEmpty()) {
            CheckoutComponent(
                state = state,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun CheckoutComponent(
    state: CartItemsState,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CartViewModel
) {
    val totalPrice = state.selectedItems.sumOf { (it.product.price * it.selectedQuantity) }
    Column(modifier = modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${state.selectedItems.size} items")
            Text(
                text = "$$totalPrice",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(5.dp))


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total")
            Text(
                text = "$$totalPrice",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val selectedItems = state.selectedItems
                val totalPrice = selectedItems.sumOf { it.product.price * it.selectedQuantity }
                // Update the shared view model
                viewModel.checkoutOrder(selectedItems, totalPrice)

                Timber.d("selected items when click checkout: $selectedItems")

                // Navigate to OrderConfirm screen
                navController.navigate("orderConfirm")
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White, containerColor = DarkBlue
            ),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = "Checkout",
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun CartItem(
    cartItem: CartProduct,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onItemSelected: (CartProduct, Boolean) -> Unit
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            // Checkbox to select the item
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onItemSelected(cartItem, it) },
                modifier = Modifier.padding(8.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = YellowMain,
                    uncheckedColor = Color.Gray
                )
            )
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = cartItem.product.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                contentScale = ContentScale.Inside,
            )

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp)
            ) {
                Text(
                    text = cartItem.product.productName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "$${cartItem.product.price}",
                    color = Color.Black,
                    fontSize = 18.sp,
                    maxLines = 3,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = "${cartItem.selectedQuantity} Pc",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
