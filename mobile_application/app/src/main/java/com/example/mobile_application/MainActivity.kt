package com.example.mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile_application.core.navigation.NavGraph
import com.example.mobile_application.core.presentation.components.CustomScaffold
import com.example.mobile_application.core.presentation.ui.theme.CustomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val newBackStackEntry by navController.currentBackStackEntryAsState()
                    val route = newBackStackEntry?.destination?.route

                    CustomScaffold(
                        navController = navController,
                        showBottomBar = route in listOf(
                            "home",
                            "wishlist",
                            "cart",
                            "account"
                        )
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") { HomeScreen(navController) }
                                composable("wishlist") { WishlistScreen(navController) }
                                composable("cart") { CartScreen(navController) }
                                composable("account") { AccountScreen(navController) }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetupNavigation() {
        val navController = rememberNavController()
        NavGraph(navController = navController)
    }
}
