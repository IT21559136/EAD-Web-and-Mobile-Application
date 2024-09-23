package com.example.mobile_application.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobile_application.core.presentation.ui.theme.DarkBlue
import com.example.mobile_application.core.presentation.ui.theme.GrayColor
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.feature_products.domain.model.BottomNavItem

@Composable
fun CustomScaffold(
    navController: NavController,
    showBottomBar: Boolean = true,
    items: List<BottomNavItem> = listOf(
        BottomNavItem.Home,
        BottomNavItem.Wishlist,
        BottomNavItem.Cart,
        BottomNavItem.Account,
    ),
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MainWhiteColor,
                    tonalElevation = 5.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null
                                )
                            },
                            selected = currentRoute == item.route,  // Directly check against item's route
                            onClick = {
                                // Navigate to the item’s route
                                navController.navigate(item.route) {
                                    // Clear back stack to the start destination
                                    navController.graph.startDestinationRoute?.let { startDestination ->
                                        popUpTo(startDestination) { saveState = true }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = DarkBlue,
                                unselectedIconColor = GrayColor
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
