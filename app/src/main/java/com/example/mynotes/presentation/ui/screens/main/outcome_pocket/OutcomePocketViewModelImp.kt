package com.example.mynotes.presentation.ui.screens.main.outcome_pocket

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.models.PocketItem
import com.example.mynotes.models.WalletItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutcomePocketViewModelImp @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val pocketUseCases: PocketUseCases,
    private val direction: OutcomePocketDirection
) : ViewModel(), OutcomePocketViewModel {

    val mapPocket: Flow<HashMap<String, PocketItem>> = flow {
        combine(pockets, wallets, currencies) { p, w, c ->
            val map = HashMap<String, PocketItem>()
            p.forEach { pocket ->
                map.put(
                    pocket.id, PocketItem(
                        name = pocket.name,
                        wallets = w.filter { it.ownerId == pocket.id }.map { wallet ->
                            WalletItem(
                                currencyName = c.first { wallet.currencyId == it.id }.name,
                                balance = wallet.balance,
                                date = wallet.date,
                            )
                        },
                    )
                )
            }
            emit(map)
        }.collect()
    }

    val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }
    val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    override fun navigateToOutcomeCurrency(pocketDomain: PocketDomain) {
        viewModelScope.launch { direction.navigateToCurrency(pocketDomain.id) }
    }
}