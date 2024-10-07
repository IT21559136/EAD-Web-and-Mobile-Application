package com.example.mobile_application.feature_products.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("availableQuantity")
    val availableQuantity: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
)