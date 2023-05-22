package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.main.convertation.ConvertationDirection
import com.example.mynotes.presentation.ui.screens.main.getcredit.GetCreditDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ConvertationDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : ConvertationDirection {
    override suspend fun back() {
        navigator.back()
    }
}