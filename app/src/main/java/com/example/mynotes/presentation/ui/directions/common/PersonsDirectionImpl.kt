package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.persons.PersonsDirection
import com.example.mynotes.presentation.ui.screens.main.persons.PersonsScreen
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PersonsDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : PersonsDirection {
    override suspend fun back() {
        navigator.back()
    }

    override suspend fun navigateToPerson(person: PersonDomain) {
        navigator.navigateTo(appScreens.personScreen(person))
    }
}