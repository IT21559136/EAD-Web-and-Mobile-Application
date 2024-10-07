package com.example.mobile_application.feature_products.data.remote.mappers

import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import com.example.mobile_application.feature_products.data.remote.dto.RatingDto
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.domain.model.Rating
import com.google.gson.annotations.SerializedName

internal fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        productName = productName,
        price = price,
        availableQuantity = availableQuantity,
        category = category,
        description = description,
        image = image
    )
}

internal fun RatingDto.toDomain(): Rating {
    return Rating(
        count = count,
        rate = rate
    )
}