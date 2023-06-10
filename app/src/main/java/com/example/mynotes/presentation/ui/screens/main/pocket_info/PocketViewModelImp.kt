package com.example.mynotes.presentation.ui.screens.main.pocket_info

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.*
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PocketViewModelImp @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val historyUseCase: TransactionUseCases,
    private val direction: PocketDirection,
) : ViewModel(), PocketViewModel {

    override val pocket: MutableState<PocketDomain> = mutableStateOf(PocketDomain(""))

    override val walletsByOwners: Flow<List<WalletOwnerDomain>> = flow {
        emitAll(walletUseCases.getWalletsByOwnerId.invoke(pocket.value.id))
    }
    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.getHistoryById.invoke(pocket.value.id))
    }

    override fun setPocket(pocket: PocketDomain) {
        viewModelScope.launch { this@PocketViewModelImp.pocket.value = pocket }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

}