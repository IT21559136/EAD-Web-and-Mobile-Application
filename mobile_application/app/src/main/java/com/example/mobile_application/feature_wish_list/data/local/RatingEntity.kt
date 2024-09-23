package com.example.mobile_application.feature_wish_list.data.local

import androidx.room.Entity
import com.example.mobile_application.feature_wish_list.util.Constant.RATING_TABLE_NAME

@Entity(tableName = RATING_TABLE_NAME)
data class RatingEntity(
    val count: Int,
    val rate: Double
)