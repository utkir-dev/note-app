package com.example.mynotes.presentation.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionGetHistory
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImp @Inject constructor(
    private val direction: HomeDirection,
    private val signOutUseCase: SignOutUseCase,
    private val currencyUseCase: CurrencyUseCases,
    private val personUseCases: PersonUseCases,
    private val walletUseCases: WalletUseCases,
    private val historyUseCase: TransactionGetHistory,
    private val pocketUseCases: PocketUseCases
) : ViewModel(), HomeViewModel {

    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }
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

    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.invoke())
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