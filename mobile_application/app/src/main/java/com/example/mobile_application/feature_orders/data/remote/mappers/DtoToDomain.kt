package com.example.mobile_application.feature_orders.data.remote.mappers

import com.example.mobile_application.feature_orders.data.remote.dto.OrderDto
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_products.data.remote.dto.RatingDto
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.domain.model.Rating

internal fun OrderDto.toDomain(): Order {
    return Order(
        id = id,
        productName = productName,
        image = image,
        price = price,
        quantity= quantity,
        status = status,
        isReviewed = isReviewed
    )
}