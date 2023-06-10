package com.example.mynotes.presentation.ui.screens.main.person_info

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModelImp @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val historyUseCase: TransactionUseCases,
    private val direction: PersonDirection,
) : ViewModel(), PersonViewModel {

    override val person: MutableState<PersonDomain> = mutableStateOf(PersonDomain(""))

    override val walletsByOwners: Flow<List<WalletOwnerDomain>> = flow {
        emitAll(walletUseCases.getWalletsByOwnerId.invoke(person.value.id))
    }
    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.getHistoryById.invoke(person.value.id))
    }

    override fun setPerson(person: PersonDomain) {
        viewModelScope.launch { this@PersonViewModelImp.person.value = person }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

}