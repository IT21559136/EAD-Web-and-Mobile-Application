package com.example.mobile_application.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobile_application.feature_auth.presentation.auth_dashboard.AuthDashboardScreen
import com.example.mobile_application.feature_auth.presentation.login.LoginScreen
import com.example.mobile_application.feature_auth.presentation.register.RegisterScreen
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.presentation.cart.CartScreen
import com.example.mobile_application.feature_cart.presentation.cart.CartViewModel
import com.example.mobile_application.feature_cart.presentation.cart.OrderConfirmScreen
import com.example.mobile_application.feature_orders.presentation.OrderScreen
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.presentation.home.HomeScreen
import com.example.mobile_application.feature_products.presentation.product_details.ProductDetailsScreen
import com.example.mobile_application.feature_products.presentation.product_details.ProductDetailsViewModel
import com.example.mobile_application.feature_profile.presentation.AccountScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()
    NavHost(navController = navController, startDestination = "auth_dashboard") {
        composable("auth_dashboard") { AuthDashboardScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("account") { AccountScreen(navController) }
        composable("orders") { OrderScreen(navController) }
        composable("cart") { CartScreen(navController, cartViewModel) }
        composable("orderConfirm") { OrderConfirmScreen(navController, cartViewModel)}


        // Product Details Route (with productId as argument)
        composable(
            route = "product_details/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType // Define the type of the argument (in this case, an Int)
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            val viewModel: ProductDetailsViewModel = hiltViewModel() // Replace with your ViewModel

            // Use 'mutableStateOf' without 'by' delegation
            val productState = remember { mutableStateOf<Product?>(null) } // Mutable state for product
            val coroutineScope = rememberCoroutineScope()

            // Fetch the product inside LaunchedEffect
            LaunchedEffect(productId) {
                coroutineScope.launch {
                    productState.value = viewModel.getProductById(productId) // Update state directly
                }
            }

            // Check the product state and display either the product details or a loading indicator
            if (productState.value != null) {
                // Show product details once the product is fetched
                ProductDetailsScreen(product = productState.value!!, navController = navController)
            } else {
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}
