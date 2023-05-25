package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModelImp @Inject constructor(
    private val direction: BalanceDirection,
    private val walletUseCases: WalletUseCases,
) : BalanceViewModel, ViewModel() {

    override val balances: Flow<List<BalanceDomain>> = flow {
        emitAll(walletUseCases.getBalances.invoke())
    }

    override fun back() {
        viewModelScope.launch {
            direction.back()
        }
    }
}