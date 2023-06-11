package com.example.mynotes.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.domain.use_cases.auth_use_case.SignOutUseCase
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.device_use_case.DeviceUseCases
import com.example.mynotes.domain.use_cases.shared_pref_use_case.SharedPrefUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.contstants.HISTORY_LIMIT
import com.example.mynotes.contstants.KEY_NIGHT_MODE
import com.example.mynotes.contstants.obj
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImp @Inject constructor(
    private val direction: HomeDirection,
    private val signOutUseCase: SignOutUseCase,
    private val walletUseCases: WalletUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val historyUseCase: TransactionUseCases,
    private val deviceUseCases: DeviceUseCases,
    private val dataUseCases: DataUseCases,
    private val shared: SharedPrefUseCases
) : ViewModel(), HomeViewModel {
  //  var isLoading = MutableStateFlow(true)
    fun checkData() {
        viewModelScope.launch(Dispatchers.IO) {
            obj.firstShown = true
//            dataUseCases.download.invoke().collect { ch ->
//                if (ch) {
//                    obj.firstShown = true
//                    isLoading.let { it.value = ch }
//                }
//            }
        }
    }

    fun observeDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            deviceUseCases.observeDevice.invoke().collect {
                if (it) {
                    exit()
                }
            }
        }
    }


    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }
    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
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
                DirectionType.CHANGE_NIGHT_MODE -> {
                    shared.saveBoolen.invoke(
                        KEY_NIGHT_MODE,
                        !shared.getBoolean.invoke(KEY_NIGHT_MODE)
                    )
                }

                DirectionType.BACK -> {
                }
                DirectionType.SETTINGS -> {
                    direction.navigateToSettings()
                }
                DirectionType.SHARE -> {
                    direction.navigateToShare()
                }
                else -> {}
            }
        }
    }

    override fun checkNotUploads() {
        viewModelScope.launch {
            dataUseCases.checkAllData.invoke()
        }
    }

    private suspend fun exit() {
        signOutUseCase.invoke()
        dataUseCases.clearDbLocal.invoke()
        direction.replaceToSignIn()
    }


}