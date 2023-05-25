package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.PersonDomain
import kotlinx.coroutines.flow.Flow


interface PersonsViewModel {
    val person: MutableState<PersonDomain>
    fun setPerson(person: PersonDomain)
    fun add(person: PersonDomain)
    fun back()
    val persons: Flow<List<PersonDomain>>
}