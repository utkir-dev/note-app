package com.example.mynotes.presentation.ui.directions

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignInDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : SignInDirection {
    override suspend fun navigateToSignUp() {
        navigator.navigateTo(appScreens.signUpScreen())
    }

    override suspend fun replaceToHome() {
        navigator.replace(appScreens.homeScreen())
    }
}