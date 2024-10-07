package com.example.mobile_application.feature_auth.di

import com.example.mobile_application.core.util.Constants.BASE_URL
import com.example.mobile_application.core.util.getUnsafeOkHttpClient
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_auth.data.remote.AuthApiService
import com.example.mobile_application.feature_auth.data.repository.AuthRepositoryImpl
import com.example.mobile_application.feature_auth.domain.repository.AuthRepository
import com.example.mobile_application.feature_auth.domain.use_case.AutoLoginUseCase
import com.example.mobile_application.feature_auth.domain.use_case.LoginUseCase
import com.example.mobile_application.feature_auth.domain.use_case.LogoutUseCase
import com.example.mobile_application.feature_auth.domain.use_case.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        authPreferences: AuthPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApiService = authApiService,
            authPreferences = authPreferences
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAutoLoginUseCase(authRepository: AuthRepository): AutoLoginUseCase {
        return AutoLoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }
}
