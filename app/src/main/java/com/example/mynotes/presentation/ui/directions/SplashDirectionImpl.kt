package com.example.mynotes.presentation.ui.directions

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.splash.SplashDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SplashDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : SplashDirection {
    override suspend fun navigateToHome() {
        navigator.replace(appScreens.homeScreen())
    }

    override suspend fun navigateToSignIn() {
        navigator.replace(appScreens.signInScreen())

    }

}