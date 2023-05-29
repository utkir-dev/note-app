package com.example.mynotes.presentation.ui.screens.main.person_info

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import kotlinx.coroutines.flow.Flow


interface PersonViewModel {
    val person: MutableState<PersonDomain>
    val walletsByOwners: Flow<List<WalletOwnerDomain>>
    fun setPerson(person: PersonDomain)
    fun back()
}