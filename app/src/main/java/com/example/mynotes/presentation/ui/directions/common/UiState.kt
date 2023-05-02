package com.example.mynotes.presentation.ui.directions.common

interface UiState {
    object Default : UiState
    object Progress : UiState
    class Error(val message: String) : UiState
}