package com.example.mynotes.presentation.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResponseResult
import com.example.mynotes.presentation.ui.directions.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImp @Inject constructor(
    private val direction: SignUpDirection,
    private val useCase: com.example.mynotes.domain.use_cases.auth_use_case.SignUpUseCase
) : ViewModel(), SignUpViewModel {

    override val uiState = MutableStateFlow<UiState>(UiState.Default)

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
                    direction.back()
                }
            }
        }
    }
}