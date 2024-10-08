package com.example.mobile_application.feature_orders.domain.model

import com.google.type.DateTime

data class Order(
    val id: String,
    val customerId: String,
    val items: List<OrderItem>,
    val customerNote: String,
    val status:String,
    val orderDate: String,
    val vendorStatuses: List<VendorStatus>
)