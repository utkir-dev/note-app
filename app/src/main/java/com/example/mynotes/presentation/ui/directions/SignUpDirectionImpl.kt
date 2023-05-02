package com.example.mynotes.presentation.ui.directions

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInDirection
import com.example.mynotes.presentation.ui.screens.auth.signup.SignUpDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignUpDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
) : SignUpDirection {
    override suspend fun back() {
        navigator.back()
    }
}