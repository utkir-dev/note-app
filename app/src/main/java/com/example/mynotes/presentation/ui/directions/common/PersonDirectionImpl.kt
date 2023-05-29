package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.main.person_info.PersonDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PersonDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PersonDirection {
    override suspend fun back() {
        navigator.back()
    }

}