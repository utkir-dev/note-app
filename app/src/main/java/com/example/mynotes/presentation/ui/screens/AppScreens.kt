package com.example.mynotes.presentation.ui.screens


import com.example.mynotes.presentation.ui.screens.auth.signin.SignInScreen
import com.example.mynotes.presentation.ui.screens.auth.signup.SignUpScreen
import com.example.mynotes.presentation.ui.screens.main.balance.BalanceScreen
import com.example.mynotes.presentation.ui.screens.main.home.HomeScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppScreens @Inject constructor() {
    fun signInScreen(): SignInScreen = SignInScreen()
    fun signUpScreen(): SignUpScreen = SignUpScreen()
    fun homeScreen(): HomeScreen = HomeScreen()
    fun balanceScreen(): BalanceScreen = BalanceScreen()
}