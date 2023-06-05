package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case.PersonUseCases
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

    val walletsByOwners: Flow<List<WalletOwnerDomain>> = flow {
        emitAll(walletUseCases.getWalletsByOwnes.invoke())
    }
    override val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personUseCases.getAll.invoke())
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

    override fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            personUseCases.delete.invoke(getPerson())
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    override fun navigateToPerson(person: PersonDomain) {
        viewModelScope.launch {
            direction.navigateToPerson(person)
        }
    }

    override var wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }

    override val person = MutableStateFlow(PersonDomain(""))
    fun getPerson() = person.value
    fun savePerson(name: String, phone: String, address: String) {
        if (person.value.name != name
            || person.value.phone != phone
            || person.value.address != address
        ) {
            val personNew = if (person.value.isValid()) {
                person.value.copy(
                    name = name,
                    phone = phone,
                    address = address,
                    date = System.currentTimeMillis(),
                    uploaded = false
                )
            } else PersonDomain(
                id = UUID.randomUUID().toString(),
                name = name,
                phone = phone,
                address = address,
                date = System.currentTimeMillis()
            )
            add(personNew)
        }
    }
}