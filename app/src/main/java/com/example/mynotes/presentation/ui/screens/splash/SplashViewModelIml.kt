package com.example.mynotes.presentation.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.use_cases.auth_use_case.GetCurrentUserUseCase
import com.example.mynotes.domain.use_cases.device_use_case.DeviceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelIml @Inject constructor(
    private val direction: SplashDirection,
    private val getCurrentUser: GetCurrentUserUseCase,

    ) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(2000)
            if (getCurrentUser.invoke() != null) {
                direction.navigateToHome()
            } else {
                direction.navigateToSignIn()
            }
        }
    }
}