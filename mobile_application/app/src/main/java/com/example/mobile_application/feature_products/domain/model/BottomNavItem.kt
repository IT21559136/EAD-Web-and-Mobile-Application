package com.example.mobile_application.feature_products.domain.model

import com.example.mobile_application.R

sealed class BottomNavItem(var icon: Int, var route: String) {
    data object Home : BottomNavItem(
        icon = R.drawable.ic_home,
        route = "home" // Adjust to your actual route name
    )

    data object Orders : BottomNavItem(
        icon = R.drawable.ic_shopping_bag,
        route = "orders" // Adjust to your actual route name
    )

    data object Cart : BottomNavItem(
        icon = R.drawable.ic_basket,
        route = "cart" // Adjust to your actual route name
    )

    data object Account : BottomNavItem(
        icon = R.drawable.ic_user,
        route = "account" // Adjust to your actual route name
    )
}
