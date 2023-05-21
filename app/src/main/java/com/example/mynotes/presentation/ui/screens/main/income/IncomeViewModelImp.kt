package com.example.mynotes.presentation.ui.screens.main.income

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Type
import com.example.common.getTypeNumber
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.TransactionDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionIncomeOutcome
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.models.PocketItem
import com.example.mynotes.models.WalletItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class IncomeViewModelImp @Inject constructor(
    private val pocketUseCases: PocketUseCases,
    private val currencyUseCases: CurrencyUseCases,
    private val walletUseCases: WalletUseCases,
    private val transactionAdd: TransactionIncomeOutcome,
    private val direction: IncomeDirection
) : ViewModel(), IncomeViewModel {

    override val pocket: MutableState<PocketDomain> = mutableStateOf(PocketDomain(""))
    override val currency: MutableState<CurrencyDomain> = mutableStateOf(CurrencyDomain(""))

    override val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCases.getAll.invoke())
    }

    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
        pocket.value = PocketDomain("")
    }

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

    val wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }


    override fun setPocket(pocket: PocketDomain) {
        viewModelScope.launch { this@IncomeViewModelImp.pocket.value = pocket }
    }

    override fun setCurrency(currency: CurrencyDomain) {
        viewModelScope.launch { this@IncomeViewModelImp.currency.value = currency }
    }

    override fun add(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            pockets.collect {
                val size =
                    it.filter { it.name.lowercase(Locale.ROOT) == pocket.name.lowercase(Locale.ROOT) }.size
                if (size == 0) {
                    pocketUseCases.add.invoke(pocket)
                }
            }
        }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    override fun addTransaction(amountTransaction: Double, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val transaction = TransactionDomain(
                id = UUID.randomUUID().toString(),
                type = getTypeNumber(Type.INCOME),
                fromId = "",
                toId = pocket.value.id,
                currencyId = currency.value.id,
                amount = amountTransaction,
                date = System.currentTimeMillis(),
                comment = comment
            )
            transactionAdd.invoke(transaction)
        }
    }
}