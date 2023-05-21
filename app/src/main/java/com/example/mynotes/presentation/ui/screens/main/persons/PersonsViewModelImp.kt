package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.models.PocketItem
import com.example.mynotes.models.WalletItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonsViewModelImp @Inject constructor(
    private val personUseCases: PersonUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val walletUseCases: WalletUseCases,
    private val direction: PersonsDirection,
) : ViewModel(), PersonsViewModel {

    override val person: MutableState<PersonDomain> = mutableStateOf(PersonDomain(""))


    override val currency: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))

    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }


    override val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personUseCases.getAll.invoke())
        person.value = PersonDomain("")
    }

    val mapPerson: Flow<HashMap<String, PocketItem>> = flow {
        combine(persons, wallets, currencies) { p, w, c ->
            val map = HashMap<String, PocketItem>()
            p.forEach { pocket ->
                map.put(
                    pocket.id, PocketItem(
                        name = pocket.name,
                        wallets = w.filter { it.ownerId == pocket.id }.map { wallet ->
                            WalletItem(
                                currencyName = c.first { wallet.currencyId == it.id }.name,
                                balance = wallet.balance,
                                date = wallet.date,
                            )
                        },
                    )
                )
            }
            emit(map)
        }.collect()
    }

    val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }


    override fun setPerson(person: PersonDomain) {
        viewModelScope.launch { this@PersonsViewModelImp.person.value = person }
    }

    override fun setCurrency(currency: CurrencyDomain) {
        viewModelScope.launch { this@PersonsViewModelImp.currency.value = currency }
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