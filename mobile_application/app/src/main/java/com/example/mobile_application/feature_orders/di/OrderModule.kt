package com.example.mobile_application.di

import com.example.mobile_application.core.util.Constants.BASE_URL
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_orders.data.remote.OrderApiService
import com.example.mobile_application.feature_orders.data.repository.OrderRepositoryImpl
import com.example.mobile_application.feature_orders.domain.repository.OrderRepository
import com.example.mobile_application.feature_orders.domain.use_case.GetOrdersUseCase
import com.example.mobile_application.feature_orders.domain.use_case.OrderStatusUseCase
import com.example.mobile_application.feature_orders.domain.use_case.OrderReviewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {
    @Provides
    @Singleton
    fun provideOrderApiService(): OrderApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        apiService: OrderApiService,
        authPreferences: AuthPreferences
    ): OrderRepository {
        return OrderRepositoryImpl(apiService, authPreferences)
    }

    @Provides
    @Singleton
    fun provideGetOrdersUseCase(repository: OrderRepository): GetOrdersUseCase {
        return GetOrdersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMarkOrderDeliveredUseCase(repository: OrderRepository): OrderStatusUseCase {
        return OrderStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddOrderReviewUseCase(repository: OrderRepository): OrderReviewUseCase {
        return OrderReviewUseCase(repository)
    }
}
