package com.example.mobile_application.feature_cart.data.remote.mappers

import com.example.mobile_application.feature_cart.data.remote.dto.CartProductDto
import com.example.mobile_application.feature_cart.data.remote.dto.UserCartDto
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.model.UserCart
import com.example.mobile_application.feature_products.domain.model.Product

internal fun CartProductDto.toDomain(): Product {
    return Product(
        id = productId,
        productName = productName,
        price = price,
        availableQuantity= availableQuantity, // Use available quantity for Product
        category = category,
        description = description,
        image = image
    )
}
internal fun UserCartDto.toDomain(): UserCart {
    return UserCart(
        customerId = customerId,
        cartProducts = items.map { CartProduct(it.toDomain(), it.quantity) }, // Use CartProduct to wrap Product and quantity
        totalPrice = totalPrice
    )
}