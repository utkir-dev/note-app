package com.example.mynotes.presentation.ui.screens.main.givecredit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Type
import com.example.common.getTypeNumber
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionDebetCredit
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GiveCreditViewModelImp @Inject constructor(
    private val personUseCases: PersonUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val pocketUseCases: PocketUseCases,
    private val walletUseCases: WalletUseCases,
    private val transactionUseCase: TransactionDebetCredit,
    private val direction: GiveCreditDirection,
) : ViewModel(), GiveCreditViewModel {

    override val person = MutableStateFlow(PersonDomain(""))

    override val currency = MutableStateFlow(CurrencyDomain(""))

    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }
    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }
    override val pocket = MutableStateFlow(PocketDomain(""))

    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }


    override val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personUseCases.getAll.invoke())
        person.value = PersonDomain("")
    }

    override val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }


    override fun setPerson(person: PersonDomain) {
        viewModelScope.launch { this@GiveCreditViewModelImp.person.value = person }
    }

    override fun setCurrency(currency: CurrencyDomain) {
        viewModelScope.launch { this@GiveCreditViewModelImp.currency.value = currency }
    }

    override fun setPocket(pocket: PocketDomain) {
        viewModelScope.launch { this@GiveCreditViewModelImp.pocket.value = pocket }
    }

    override fun addTransaction(amountTransaction: Double, comment: String, balance: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val time = System.currentTimeMillis()
            val transaction = TransactionDomain(
                id = time.toString(),
                type = getTypeNumber(Type.DEBET),
                fromId = pocket.value.id,
                toId = person.value.id,
                currencyId = currency.value.id,
                amount = amountTransaction,
                date = time,
                comment = comment,

                isFromPocket = true,
                isToPocket = false,
                rate = currency.value.rate,
                rateFrom = currency.value.rate,
                rateTo = currency.value.rate,
                balance = balance
            )
            this@GiveCreditViewModelImp.transactionUseCase.invoke(transaction)
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

}