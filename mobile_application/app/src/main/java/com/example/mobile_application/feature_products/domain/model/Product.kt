package com.example.mobile_application.feature_products.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val category: String,
    val description: String,
    val image: String,
) : Parcelable
