package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.pockets.PocketsDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PocketsDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : PocketsDirection {
    override suspend fun back() {
        navigator.back()
    }

    override suspend fun navigateToPocket(pocket: PocketDomain) {
        navigator.navigateTo(appScreens.getPocketScreen(pocket))
    }
}