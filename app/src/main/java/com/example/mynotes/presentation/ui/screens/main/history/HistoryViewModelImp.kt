package com.example.mynotes.presentation.ui.screens.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.getTypeText
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.TransactionDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionGetAll
import com.example.mynotes.models.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImp @Inject constructor(
    private val direction: HistoryDirection,
    private val currencyUseCase: CurrencyUseCases,
    private val historyUseCase: TransactionGetAll,
    private val pocketUseCases: PocketUseCases,
    private val personsUseCases: PersonUseCases
) : ViewModel(), HistoryViewModel {


    private val persons: Flow<List<PersonDomain>> = flow {
        emitAll(personsUseCases.getAll.invoke())
    }
    private val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }
    private val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(currencyUseCase.getAll.invoke())
    }
    private val transactions: Flow<List<TransactionDomain>> = flow {
        emitAll(historyUseCase.invoke())
    }

    override val historyList: Flow<List<HistoryItem>> = flow {
        combine(transactions, persons, pockets, currencies) { t, personList, pocketList, c ->
            val list = mutableListOf<HistoryItem>()
            t.forEach { trans ->
                var nameFrom = ""
                if (trans.fromId.isNotEmpty()) {
                    val from = pocketList.filter { it.id == trans.fromId }
                    nameFrom =
                        if (from.size > 0) from.first().name + " hamyondan" else personList.first { it.id == trans.fromId }.name + "dan"
                }
                var nameTo = ""
                if (trans.toId.isNotEmpty()) {
                    val to = pocketList.filter { it.id == trans.toId }
                    nameTo =
                        if (to.size > 0) to.first().name + " hamyonga" else personList.first { it.id == trans.toId }.name + "ga"
                }
                list.add(
                    HistoryItem(
                        title = getTypeText(trans.type),
                        amount = trans.amount,
                        currency = c.first { it.id == trans.currencyId }.name,
                        from = nameFrom,
                        to = nameTo,
                        comment = trans.comment,
                        date = trans.date,
                    )
                )
            }
            emit(list)
        }//.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
            .collect()
    }

    override fun back() {
        viewModelScope.launch { direction.back() }
    }

}