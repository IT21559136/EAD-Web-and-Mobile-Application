package com.example.mobile_application.feature_auth.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return authRepository.logout()
    }
}
