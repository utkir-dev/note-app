package com.example.mynotes.presentation.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.Response
import com.example.mynotes.domain.use_cases.auth_use_case.SignInUseCase
import com.example.mynotes.domain.use_cases.auth_use_case.SignUpUseCase
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.directions.common.UiState
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImp @Inject constructor(
    private val direction: SignUpDirection,
    private val useCase: SignUpUseCase
) : ViewModel(), SignUpViewModel {

    override val uiState = MutableStateFlow<UiState>(UiState.Default)

    override fun signUp(login: String, password: String) {
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
                    direction.back()
                }
            }
        }
    }
}