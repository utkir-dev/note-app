package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface PersonsViewModel {
    val person: Flow<PersonDomain>
    fun setPerson(person: PersonDomain)
    fun add(person: PersonDomain)
    fun delete()
    fun back()
    fun navigateToPerson(person: PersonDomain)

    val persons: Flow<List<PersonDomain>>
    var wallets: Flow<List<WalletDomain>>
}