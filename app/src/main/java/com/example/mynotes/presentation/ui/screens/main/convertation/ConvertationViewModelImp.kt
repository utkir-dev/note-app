package com.example.mynotes.presentation.ui.screens.main.convertation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Type
import com.example.common.getTypeNumber
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConvertationViewModelImp @Inject constructor(
    private val currencyUseCases: CurrencyUseCases,
    private val pocketUseCases: PocketUseCases,
    private val walletUseCases: WalletUseCases,
    private val transactionUseCase: TransactionUseCases,
    private val direction: ConvertationDirection,
) : ViewModel(), ConvertationViewModel {

    override val currencyFrom: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))
    override val currencyTo: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))
    override val currency: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))
    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }

    override val pocketFrom: MutableState<PocketDomain> = mutableStateOf(PocketDomain(""))
    override val pocketTo: MutableState<PocketDomain> = mutableStateOf(PocketDomain(""))
    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }

    override val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }

    override fun setCurrencyFrom(currency: CurrencyDomain) {
        viewModelScope.launch { this@ConvertationViewModelImp.currencyFrom.value = currency }
    }

    override fun setCurrencyTo(currency: CurrencyDomain) {
        viewModelScope.launch { this@ConvertationViewModelImp.currencyTo.value = currency }

    }

    override fun setCurrency(currency: CurrencyDomain) {
        viewModelScope.launch { this@ConvertationViewModelImp.currency.value = currency }
    }

    override fun setPocketFrom(pocket: PocketDomain) {
        viewModelScope.launch { this@ConvertationViewModelImp.pocketFrom.value = pocket }
    }

    override fun setPocketTo(pocket: PocketDomain) {
        viewModelScope.launch { this@ConvertationViewModelImp.pocketTo.value = pocket }

    }

    private fun addTransaction(
        amountTransaction: Double,
        walletFrom: WalletDomain,
        amountDollar: Double,
        comment: String = "",
        balance: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = TransactionDomain(
                id = UUID.randomUUID().toString(),
                type = getTypeNumber(Type.CONVERTATION),
                fromId = pocketFrom.value.id,
                toId = pocketTo.value.id,
                currencyId = currency.value.id,
                amount = amountTransaction,
                currencyFrom = currencyFrom.value.id,
                currencyTo = currencyTo.value.id,
                date = System.currentTimeMillis(),
                comment = comment,

                isFromPocket = true,
                isToPocket = true,
                rate = currency.value.rate,
                rateFrom = currencyFrom.value.rate,
                rateTo = currencyTo.value.rate,
                balance = balance
            )
            this@ConvertationViewModelImp.transactionUseCase.getConvertation.invoke(
                trans = transaction,
                currencyConvert = currency.value,
                fromWallet = walletFrom,
                curFrom = currencyFrom.value,
                curTo = currencyTo.value,
                amountDollar = amountDollar,
            )
        }
    }

    val isValid: MutableState<Boolean> = mutableStateOf(false)

    fun validateTransaction(
        amount: String, walletFrom: WalletDomain?, comment: String,
        balance: Double
    ) {
        viewModelScope.launch {
            var n = -1.0
            try {
                n = amount.trim().toDouble()
                walletFrom?.let { wallet ->
                    val amountDollar = (1 / currency.value.rate) * n
                    val balanceDollar = wallet.balance / currencyFrom.value.rate
                    if (balanceDollar >= amountDollar) {
                        addTransaction(n, wallet, amountDollar, comment, balance)
                    }
                    isValid.value = balanceDollar >= amountDollar
                }
            } catch (_: Exception) {
                isValid.value = false
            }
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

}