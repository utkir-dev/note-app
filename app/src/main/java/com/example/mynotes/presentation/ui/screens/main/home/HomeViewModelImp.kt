package com.example.mynotes.presentation.ui.screens.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.getTypeText
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionGetForHome
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.models.HistoryItem
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImp @Inject constructor(
    private val direction: HomeDirection,
    private val signOutUseCase: SignOutUseCase,
    private val currencyUseCase: CurrencyUseCases,
    private val personUseCases: PersonUseCases,
    private val walletUseCases: WalletUseCases,
    private val historyUseCase: TransactionGetForHome,
    private val pocketUseCases: PocketUseCases
) : ViewModel(), HomeViewModel {

    private val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }
    val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personUseCases.getAll.invoke())
    }
    val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCase.getAll.invoke())
    }
    private val transactions: Flow<List<TransactionDomain>> = flow {
        emitAll(historyUseCase.invoke(HISTORY_LIMIT))
    }

    val historyList: Flow<List<HistoryItem>> = flow {
        combine(transactions, persons, pockets, currencies) { t, personList, pocketList, c ->
            val list = mutableListOf<HistoryItem>()
            t.forEach { trans ->
                var nameFrom = ""
                if (trans.fromId.isNotEmpty()) {
                    val from = pocketList.filter { it.id == trans.fromId }
                    nameFrom =
                        if (from.size > 0) from.first().name + " hamyondan" else personList.first { it.id == trans.fromId }.name + "dan"
                }
                var nameTo = ""
                if (trans.toId.isNotEmpty()) {
                    val to = pocketList.filter { it.id == trans.toId }
                    nameTo =
                        if (to.size > 0) to.first().name + " hamyonga" else personList.first { it.id == trans.toId }.name + "ga"
                }
                list.add(
                    HistoryItem(
                        title = getTypeText(trans.type),
                        amount = trans.amount,
                        currency = c.first { it.id == trans.currencyId }.name,
                        from = nameFrom,
                        to = nameTo,
                        comment = trans.comment,
                        date = trans.date,
                    )
                )
            }
            emit(list)
        }//.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
            .collect()
    }

    var balance: Flow<Double> = flow {
        combine(pockets, wallets, currencies) { p, w, c ->
            if (c.isEmpty()) {
                val currency = CurrencyDomain(
                    id = UUID.randomUUID().toString(),
                    name = "dollar",
                    rate = 1.0,
                    date = System.currentTimeMillis(),
                )
                currencyUseCase.add.invoke(currency)
            }


            val pocketIds = p.map { it.id }
            emit(w.filter { pocketIds.contains(it.ownerId) }.sumOf { wallet ->
                wallet.balance * (1 / c.first { it.id == wallet.currencyId }.rate)
            })
        } //.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
            .collect()
    }

    override fun onEventDispatcher(type: DirectionType) {
        viewModelScope.launch {
            when (type) {
                DirectionType.BALANCE -> {
                    direction.navigateToBalance()
                }
                DirectionType.SIGNOUT -> {
                    signOutUseCase.invoke()
                    direction.navigateToSignIn()
                }
                DirectionType.INCOME -> {
                    direction.navigateToIncome()
                }
                DirectionType.OUTCOME -> {
                    direction.navigateToOutcome()
                }
                DirectionType.GETCREDIT -> {
                    direction.navigateToGetCredit()
                }
                DirectionType.GIVECREDIT -> {
                    direction.navigateToGiveCredit()
                }
                DirectionType.POCKETS -> {
                    direction.navigateToPockets()
                }

                DirectionType.PERSONS -> {
                    direction.navigateToPersons()
                }
                DirectionType.CONVERTATION -> {
                    direction.navigateToConvertation()
                }
                DirectionType.CURRENCIES -> {
                    direction.navigateToCurrencies()
                }
                DirectionType.HISTORY -> {
                    direction.navigateToHistory()
                }

                DirectionType.BACK -> {
                }
                else -> {}
            }
        }
    }
}