package com.example.mynotes.presentation.ui.screens.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.Response
import com.example.mynotes.domain.use_cases.auth_use_case.SignInUseCase
import com.example.mynotes.presentation.ui.directions.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImp @Inject constructor(
    private val direction: SignInDirection,
    private val useCase: SignInUseCase
) : ViewModel(), SignInViewModel {
    override var uiState = MutableStateFlow<UiState>(UiState.Default)

    override fun signIn(login: String, password: String) {
        uiState.value = UiState.Progress
        viewModelScope.launch {
            when (useCase.invoke(login, password)) {
                is Response.Loading -> {
                    uiState.value = UiState.Progress
                }
                is Response.Failure -> {
                    uiState.value = UiState.Error("Xatolik")
                }
                is Response.Success -> {
                    direction.replaceToHome()
                }
            }
        }
    }

    override fun signUp() {
        viewModelScope.launch { direction.navigateToSignUp() }
    }
}