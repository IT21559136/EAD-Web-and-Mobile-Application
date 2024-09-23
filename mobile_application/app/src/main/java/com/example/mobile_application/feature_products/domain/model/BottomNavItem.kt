package com.example.mobile_application.feature_products.domain.model

import com.example.mobile_application.R

sealed class BottomNavItem(var icon: Int, var route: String) {
    object Home : BottomNavItem(
        icon = R.drawable.ic_home,
        route = "home" // Adjust to your actual route name
    )

    object Wishlist : BottomNavItem(
        icon = R.drawable.ic_heart,
        route = "wishlist" // Adjust to your actual route name
    )

    object Cart : BottomNavItem(
        icon = R.drawable.ic_basket,
        route = "cart" // Adjust to your actual route name
    )

    object Account : BottomNavItem(
        icon = R.drawable.ic_user,
        route = "account" // Adjust to your actual route name
    )
}
