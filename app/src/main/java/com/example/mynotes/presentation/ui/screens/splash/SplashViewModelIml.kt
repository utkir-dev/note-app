package com.example.mynotes.presentation.ui.screens.splash

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.contstants.obj
import com.example.mynotes.domain.use_cases.auth_use_case.GetCurrentUserUseCase
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.device_use_case.DeviceUseCases
import com.example.mynotes.presentation.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelIml @Inject constructor(
    private val direction: SplashDirection,
    private val dataUseCases: DataUseCases,
    private val getCurrentUser: GetCurrentUserUseCase,

    ) : ViewModel() {
    init {
        MainActivity.isOpen.value = true
        viewModelScope.launch {
            async {
                dataUseCases.download.invoke().collect { ch ->
                    if (ch) {
                        obj.firstShown = true
                    }
                }
            }
            delay(3000)
            MainActivity.isOpen.value = false
            if (getCurrentUser.invoke() != null) {
                direction.navigateToHome()
            } else {
                direction.navigateToSignIn()
            }
        }
    }
}