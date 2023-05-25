package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonsViewModelImp @Inject constructor(
    private val personUseCases: PersonUseCases,
    private val walletUseCases: WalletUseCases,
    private val direction: PersonsDirection,
) : ViewModel(), PersonsViewModel {

    override val person: MutableState<PersonDomain> = mutableStateOf(PersonDomain(""))


    val walletsByOwners: Flow<List<WalletOwnerDomain>> = flow {
        emitAll(walletUseCases.getWalletsByOwnes.invoke())
    }
    override val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personUseCases.getAll.invoke())
    }

    val personsWithWallets: Flow<List<PersonWithWalletsDomain>> = flow {
        emitAll(personUseCases.getPersonsWithWallets.invoke())
    }


    override fun setPerson(person: PersonDomain) {
        viewModelScope.launch { this@PersonsViewModelImp.person.value = person }
    }


    override fun add(person: PersonDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            persons.collect {
                val size =
                    it.filter { it.name.lowercase(Locale.ROOT) == person.name.lowercase(Locale.ROOT) }.size
                if (size == 0) {
                    personUseCases.add.invoke(person)
                }
            }
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

}