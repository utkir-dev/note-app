package com.example.mynotes.presentation.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.data_use_case.ObserveDevice
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImp @Inject constructor(
    private val direction: HomeDirection,
    private val signOutUseCase: SignOutUseCase,
    private val walletUseCases: WalletUseCases,
    private val historyUseCase: TransactionUseCases,
    private val observeUseCase: ObserveDevice,
    private val dataUseCases: DataUseCases
) : ViewModel(), HomeViewModel {
    init {
        viewModelScope.launch {
            dataUseCases.download.invoke()
            observeUseCase.invoke().collect {
                if (it) {
                    exit()
                }
            }
        }
    }

    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }

    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.getHistoryForHome.invoke(HISTORY_LIMIT))
    }

    override fun onEventDispatcher(type: DirectionType) {
        viewModelScope.launch {
            when (type) {
                DirectionType.BALANCE -> {
                    direction.navigateToBalance()
                }
                DirectionType.SIGNOUT -> {
                    exit()
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

    private suspend fun exit() {
        signOutUseCase.invoke()
        dataUseCases.clearDbLocal.invoke()
        direction.replaceToSignIn()
    }
}