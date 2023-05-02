package com.example.mynotes.presentation.ui.screens.splash

interface SplashDirection {
   suspend fun navigateToHome()
   suspend fun navigateToSignIn()
}