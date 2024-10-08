package com.example.mobile_application.feature_orders.data.remote.dto

import com.example.mobile_application.feature_orders.domain.model.VendorStatus
import com.google.gson.annotations.SerializedName
import com.google.type.DateTime

data class OrderDto(
    @SerializedName("id")
    val orderId: String,

    @SerializedName("customerId")
    val customerId: String,

    @SerializedName("items")
    val items: List<OrderItemDto>,

    @SerializedName("status")
    val status: String,

    @SerializedName("orderDate")
    val orderDate: String,

    @SerializedName("customerNote")
    val customerNote: String,

    @SerializedName("vendorStatuses")
    val vendorStatuses: List<VendorStatus>,
)
