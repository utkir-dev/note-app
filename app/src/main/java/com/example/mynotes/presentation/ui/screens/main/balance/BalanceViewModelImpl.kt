package com.example.mynotes.presentation.ui.screens.main.balance

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.models.BalanceItem
import com.example.mynotes.models.PocketItem
import com.example.mynotes.models.WalletItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModelImp @Inject constructor(
    private val direction: BalanceDirection,
    private val pocketUseCases: PocketUseCases,
    private val walletUseCases: WalletUseCases,
    private val currencyUseCases: CurrencyUseCases
) : BalanceViewModel, ViewModel() {

    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }
    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }
    override val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    var balance: MutableState<Double> = mutableStateOf(0.0)

    val flowBalance: Flow<List<BalanceItem>> = flow {
        combine(pockets, wallets, currencies) { p, w, c ->
            val pocketIds = p.map { it.id }
            balance.value = w.filter { pocketIds.contains(it.ownerId) }.sumOf { wallet ->
                wallet.balance * (1 / c.first { it.id == wallet.currencyId }.rate)
            }
            val list = mutableListOf<BalanceItem>()
            c.forEach { currency ->
                list.add(BalanceItem(
                    name = currency.name,
                    amount = w.filter { it.currencyId == currency.id && pocketIds.contains(it.ownerId) }
                        .sumOf { it.balance }
                )
                )
            }
            emit(list)
        }.collect()
    }

    override fun back() {
        viewModelScope.launch {
            direction.back()
        }
    }
}