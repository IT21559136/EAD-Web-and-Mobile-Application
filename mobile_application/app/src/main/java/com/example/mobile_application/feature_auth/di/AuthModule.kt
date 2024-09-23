package com.example.mobile_application.feature_auth.di

import com.example.mobile_application.core.util.Constants.BASE_URL
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_auth.data.remote.AuthApiService
import com.example.mobile_application.feature_auth.data.repository.LoginRepositoryImpl
import com.example.mobile_application.feature_auth.domain.repository.LoginRepository
import com.example.mobile_application.feature_auth.domain.use_case.AutoLoginUseCase
import com.example.mobile_application.feature_auth.domain.use_case.LoginUseCase
import com.example.mobile_application.feature_auth.domain.use_case.LogoutUseCase
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        authApiService: AuthApiService,
        authPreferences: AuthPreferences
    ): LoginRepository {
        return LoginRepositoryImpl(
            authApiService = authApiService,
            authPreferences = authPreferences
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Provides
    @Singleton
    fun provideAutoLoginUseCase(loginRepository: LoginRepository): AutoLoginUseCase {
        return AutoLoginUseCase(loginRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(loginRepository: LoginRepository): LogoutUseCase {
        return LogoutUseCase(loginRepository)
    }
}
