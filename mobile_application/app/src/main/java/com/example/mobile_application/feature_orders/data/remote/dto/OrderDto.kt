package com.example.mobile_application.feature_orders.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("customerId")
    val customerId: String,

    @SerializedName("items")
    val items: List<OrderItemDto>,

    @SerializedName("customerNote")
    val customerNote: String
)
