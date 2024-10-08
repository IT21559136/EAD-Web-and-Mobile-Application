package com.example.mobile_application.feature_cart.domain.model

import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class CartProduct(
    val product: Product,  // Composing with Product
    val selectedQuantity: Int  // Additional property for selected quantity
)

fun List<CartProduct>.toJson() = Json.encodeToString(this)
fun String.fromJson(): List<CartProduct> = Json.decodeFromString(this)