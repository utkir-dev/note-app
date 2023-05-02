package com.example.mynotes.presentation.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.use_cases.auth_use_case.CheckUserUseCase
import com.example.mynotes.domain.use_cases.auth_use_case.SignInUseCase
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelIml @Inject constructor(
    private val direction: SplashDirection,
    private val useCase: CheckUserUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(2000)
            if (useCase.invoke() != null) {
                direction.navigateToHome()
            } else {
                direction.navigateToSignIn()
            }
        }
    }
}