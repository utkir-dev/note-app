package com.example.mynotes.presentation.ui.screens.auth.signup

import com.example.mynotes.presentation.ui.directions.common.UiState
import kotlinx.coroutines.flow.Flow

interface SignUpViewModel {
    val uiState: Flow<UiState>
    fun signUp(login: String, password: String)
    fun changeUiState(state: UiState)
    fun back()
}