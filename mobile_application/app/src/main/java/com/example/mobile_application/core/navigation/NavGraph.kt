package com.example.mobile_application.core.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobile_application.feature_auth.presentation.auth_dashboard.AuthDashboardScreen
import com.example.mobile_application.feature_auth.presentation.login.LoginScreen
import com.example.mobile_application.feature_auth.presentation.register.RegisterScreen
import com.example.mobile_application.feature_cart.presentation.cart.CartScreen
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.presentation.home.HomeScreen
import com.example.mobile_application.feature_products.presentation.product_details.ProductDetailsScreen
import com.example.mobile_application.feature_products.presentation.product_details.ProductDetailsViewModel
import com.example.mobile_application.feature_profile.presentation.AccountScreen
import com.example.mobile_application.feature_wish_list.presentation.WishlistScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "auth_dashboard") {
        composable("auth_dashboard") { AuthDashboardScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("account") { AccountScreen(navController) }
        composable("wishlist") { WishlistScreen(navController) }
        composable("cart") { CartScreen(navController) }

        // Product Details Route (with productId as argument)
        // Product Details Route (with productId as argument)
        composable(
            route = "product_details/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.IntType // Define the type of the argument (in this case, an Int)
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
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
                // Show a loading state or placeholder
                CircularProgressIndicator()
            }
        }
    }
}
