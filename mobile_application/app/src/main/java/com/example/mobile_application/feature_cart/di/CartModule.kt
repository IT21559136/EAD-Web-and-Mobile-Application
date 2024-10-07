package com.example.mobile_application.feature_cart.di

import com.google.gson.Gson
import com.example.mobile_application.core.util.Constants
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_cart.data.remote.CartApiService
import com.example.mobile_application.feature_cart.data.repository.CartRepositoryImpl
import com.example.mobile_application.feature_cart.domain.repository.CartRepository
import com.example.mobile_application.feature_cart.domain.use_case.DeleteCartItemsUseCase
import com.example.mobile_application.feature_cart.domain.use_case.GetCartItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCartApiService(): CartApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartApiService: CartApiService,
        authPreferences: AuthPreferences
    ): CartRepository {
        return CartRepositoryImpl(
            cartApiService,
            authPreferences
        )
    }

    @Provides
    @Singleton
    fun provideGetCartItemsUseCase(
        cartRepository: CartRepository,
    ): GetCartItemsUseCase {
        return GetCartItemsUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteCartItemsUseCase(
        cartRepository: CartRepository,
    ): DeleteCartItemsUseCase {
        return DeleteCartItemsUseCase(cartRepository)
    }
}