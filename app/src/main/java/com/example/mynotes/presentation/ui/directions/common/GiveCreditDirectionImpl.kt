package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.main.givecredit.GiveCreditDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GiveCreditDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : GiveCreditDirection {
    override suspend fun back() {
        navigator.back()
    }
}