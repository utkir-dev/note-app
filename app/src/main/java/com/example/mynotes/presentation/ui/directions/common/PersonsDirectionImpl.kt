package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.main.persons.PersonsDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PersonsDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PersonsDirection {
    override suspend fun back() {
        navigator.back()
    }
}