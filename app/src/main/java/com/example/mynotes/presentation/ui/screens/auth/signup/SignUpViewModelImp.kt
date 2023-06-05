package com.example.mynotes.presentation.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResponseResult
import com.example.mynotes.domain.use_cases.auth_use_case.SignUpUseCase
import com.example.mynotes.presentation.ui.directions.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImp @Inject constructor(
    private val direction: SignUpDirection,
    private val useCase: SignUpUseCase
) : ViewModel(), SignUpViewModel {

    override val uiState = MutableStateFlow<UiState>(UiState.Default)
    val success = MutableStateFlow(false)
    override fun changeUiState(state: UiState) {
        uiState.value = state
    }

    override fun back() {
        viewModelScope.launch {
            direction.back()
        }
    }

    override fun signUp(login: String, password: String) {
        uiState.value = UiState.Progress
        viewModelScope.launch {
            when (useCase.invoke(login, password)) {
                is ResponseResult.Loading -> {
                    uiState.value = UiState.Progress
                }
                is ResponseResult.Failure -> {
                    uiState.value = UiState.Error("Xatolik")
                }
                is ResponseResult.Success -> {
                    success.value = true

                }
            }
        }
    }
}