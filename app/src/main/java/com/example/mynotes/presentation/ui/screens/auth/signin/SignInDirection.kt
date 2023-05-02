package com.example.mynotes.presentation.ui.screens.auth.signin

interface SignInDirection {
   suspend fun navigateToSignUp()
   suspend fun replaceToHome()
}