package com.example.mynotes.presentation.ui.screens.main.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModelImp @Inject constructor(
    private val useCases: CurrencyUseCases,
    private val walletUseCases: WalletUseCases,
    private val direction: CurrencyDirection
) : ViewModel(), CurrencyViewModel {

    override var currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(useCases.getAll.invoke())
    }
    override var wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    val currency = MutableStateFlow(CurrencyDomain(""))
    fun getCurrency() = currency.value
    override fun add(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.add.invoke(currency)
        }
    }

    override fun update(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.update.invoke(currency)
        }
    }

    override fun delete() {
        viewModelScope.launch(Dispatchers.IO) {

            useCases.delete.invoke(getCurrency())
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    fun saveCurrency(name: String, rate: String) {
        if (currency.value.name != name || currency.value.rate != rate.toDouble()) {
            val currencyNew = if (currency.value.isValid()) {
                currency.value.copy(
                    name = name,
                    rate = rate.toDouble(),
                    date = System.currentTimeMillis(),
                    uploaded = false
                )
            } else CurrencyDomain(
                id = UUID.randomUUID().toString(),
                name = name,
                rate = rate.toDouble(),
                date = System.currentTimeMillis()
            )
            add(currencyNew)
        }
    }
}