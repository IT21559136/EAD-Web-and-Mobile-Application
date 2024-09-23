package com.example.mobile_application.feature_profile.di

import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_profile.data.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideProfileRepository(authPreferences: AuthPreferences): ProfileRepository {
        return ProfileRepository(authPreferences)
    }
}