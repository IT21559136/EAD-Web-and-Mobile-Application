package com.example.mobile_application.feature_profile.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_auth.data.dto.UserResponseDto
import com.example.mobile_application.feature_auth.domain.use_case.LogoutUseCase
import com.example.mobile_application.feature_profile.data.repository.ProfileRepository
import com.example.mobile_application.feature_profile.data.toDomain
import com.example.mobile_application.feature_profile.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logoutUseCase: LogoutUseCase,
    private val gson: Gson,
) : ViewModel() {

    private val _profileState = mutableStateOf(User())
    val profileState: State<User> = _profileState

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfile().collectLatest { data ->
                Timber.d("Data: $data")
                val user = gson.fromJson(data, UserResponseDto::class.java)
                _profileState.value = user.toDomain()
            }
        }
    }

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            val result = logoutUseCase()

            Timber.d("Result: ${result.message}")
            when (result) {
                is Resource.Success -> {
                    // Use raw route string instead of AuthDashboardScreenDestination.route
                    _eventFlow.emit(
                        UiEvents.NavigateEvent(route = "auth_dashboard")
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "Unknown error occurred"
                        )
                    )
                }
                else -> {}
            }
        }
    }
}
