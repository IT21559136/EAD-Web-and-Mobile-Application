package com.example.mobile_application.feature_auth.domain.use_case

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.domain.repository.LoginRepository

class AutoLoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return loginRepository.autoLogin()
    }
}