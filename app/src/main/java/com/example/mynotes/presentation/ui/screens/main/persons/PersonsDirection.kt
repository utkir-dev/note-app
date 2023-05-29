package com.example.mynotes.presentation.ui.screens.main.persons

import com.example.mynotes.domain.models.PersonDomain

interface PersonsDirection {
    suspend fun back()
    suspend fun navigateToPerson(person: PersonDomain)
}