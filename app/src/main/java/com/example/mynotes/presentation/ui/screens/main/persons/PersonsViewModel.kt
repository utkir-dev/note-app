package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow


interface PersonsViewModel {
    val person: MutableState<PersonDomain>
    val persons: Flow<List<PersonDomain>>
    val currency: MutableState<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>

    fun setPerson(person: PersonDomain)
    fun setCurrency(currency: CurrencyDomain)
    fun add(person: PersonDomain)
    fun back()
}