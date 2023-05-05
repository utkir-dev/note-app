package com.example.mynotes.presentation.ui.screens.main.home

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
import com.example.mynotes.presentation.MainActivity
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImp @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens,
    private val useCase: com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
) : ViewModel(), HomeViewModel {
    override fun onEventDispatcher(type: DirectionType) {
        viewModelScope.launch {
            when (type) {
                DirectionType.BALANCE -> {
                    navigator.navigateTo(appScreens.balanceScreen())
                }
                DirectionType.SIGNOUT -> {
                    useCase.invoke()
                    navigator.replaceAll(appScreens.signInScreen())
                }
                DirectionType.INCOME -> {}
                DirectionType.OUTCOME -> {}
                DirectionType.GETCREDIT -> {}
                DirectionType.GIVECREDIT -> {}
                DirectionType.CREDITORS -> {}
                DirectionType.DEBETORS -> {}
                DirectionType.POCKETS -> {}
                DirectionType.CURRENCIES -> {}
                DirectionType.HISTORY -> {}

                DirectionType.BACK -> {

                }
                else -> {}
            }
        }
    }
}