package com.example.mobile_application.core.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_application.feature_auth.presentation.auth_dashboard.AuthDashboardScreen
import com.example.mobile_application.feature_auth.presentation.login.LoginScreen
import com.example.mobile_application.feature_auth.presentation.register.RegisterScreen
import com.example.mobile_application.feature_cart.presentation.cart.CartScreen
import com.example.mobile_application.feature_products.presentation.home.HomeScreen
import com.example.mobile_application.feature_profile.presentation.AccountScreen
import com.example.mobile_application.feature_wish_list.presentation.WishlistScreen

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
        // Add more destinations here as needed
    }
}
