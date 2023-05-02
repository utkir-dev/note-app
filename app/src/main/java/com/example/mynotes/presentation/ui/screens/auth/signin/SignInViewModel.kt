package com.example.mynotes.presentation.ui.screens.auth.signin

import com.example.mynotes.presentation.ui.directions.common.UiState
import kotlinx.coroutines.flow.Flow

interface SignInViewModel {
    val uiState :Flow<UiState>
    fun signIn(login: String, password: String)
    fun signUp()
}