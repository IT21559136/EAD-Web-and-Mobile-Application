package com.example.mobile_application.feature_orders.data.remote.mappers

import com.example.mobile_application.feature_orders.data.remote.dto.OrderDto
import com.example.mobile_application.feature_orders.data.remote.dto.OrderItemDto
import com.example.mobile_application.feature_orders.domain.model.Order
import com.example.mobile_application.feature_orders.domain.model.OrderItem

fun OrderDto.toDomain(): Order {
    return Order(
        id = orderId,
        customerId = customerId,
        items = items.map { it.toDomain() },  // Mapping each OrderItemDto to OrderItem
        customerNote = customerNote,
        status = status,
        orderDate = orderDate,
        vendorStatuses = vendorStatuses
    )
}

fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        productId = productId,
        quantity = quantity,
        vendorEmail = vendorEmail,
        orderItemStatus = orderItemStatus
    )
}
