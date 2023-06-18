package com.example.mynotes.presentation.ui.screens.main.outcome_currency

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Type
import com.example.common.getTypeNumber
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionIncomeOutcome
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OutcomeCurrencyViewModelImp @Inject constructor(
    private val pocketCases: PocketUseCases,
    private val walletUseCases: WalletUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val transactionUseCase: TransactionIncomeOutcome,
    private val direction: OutcomeCurrencyDirection
) : ViewModel(), OutcomeCurrencyViewModel {

    override val wallet: MutableState<WalletDomain> = mutableStateOf(WalletDomain(""))
    override val pocket: MutableState<PocketDomain> = mutableStateOf(PocketDomain(""))
    override val currency: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))

    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }

    override val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getByOwnerId.invoke(pocket.value.id))
    }

    override fun setPocket(pocketId: String) {
        viewModelScope.launch {
            pocket.value = pocketCases.getOne.invoke(pocketId)
        }
    }

    override fun setCurrency(currency: CurrencyDomain) {
        viewModelScope.launch { this@OutcomeCurrencyViewModelImp.currency.value = currency }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }

    override fun addTransaction(amountTransaction: Double, comment: String, balance: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val time = System.currentTimeMillis()
            val transaction = TransactionDomain(
                id = time.toString(),
                type = getTypeNumber(Type.OUTCOME),
                fromId = pocket.value.id,
                toId = "",
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
            transactionUseCase.invoke(transaction)
        }
    }

    override fun setWallet(value: WalletDomain) {
        viewModelScope.launch { wallet.value = value }
    }
}