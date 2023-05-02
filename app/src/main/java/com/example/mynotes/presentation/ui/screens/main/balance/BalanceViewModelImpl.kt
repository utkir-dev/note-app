package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class BalanceViewModelImp @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : BalanceViewModel, ViewModel() {
    override fun onEventDispatcher(type: DirectionType) {
        viewModelScope.launch {
//            when (type) {
//                is DirectionType.HOME -> {
//                    navigator.navigateTo(appScreens.homeScreen())
//                }
//                else -> {}
//            }
        }
    }
}