package com.example.mynotes.presentation.ui.screens.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResponseResult
import com.example.mynotes.domain.use_cases.auth_use_case.SignInUseCase
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.device_use_case.DeviceUseCases
import com.example.mynotes.presentation.ui.directions.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImp @Inject constructor(
    private val direction: SignInDirection,
    private val useCase: SignInUseCase,
    private val deviceUseCases: DeviceUseCases,
) : ViewModel(), SignInViewModel {
    override var uiState = MutableStateFlow<UiState>(UiState.Default)
    fun changeUiState(state: UiState) {
        uiState.value = state
    }

    override fun signIn(login: String, password: String) {
        uiState.value = UiState.Progress
        viewModelScope.launch {
            when (useCase.invoke(login, password)) {
                is ResponseResult.Loading -> {
                    uiState.value = UiState.Progress
                }
                is ResponseResult.Failure -> {
                    uiState.value = UiState.Error("Xatolik")
                }
                is ResponseResult.Success<*> -> {
                    runBlocking { deviceUseCases.saveDevice.invoke() }
                    direction.replaceToHome()
                }
                else -> {}
            }
        }
    }

    override fun signUp() {
        viewModelScope.launch { direction.navigateToSignUp() }
    }
}