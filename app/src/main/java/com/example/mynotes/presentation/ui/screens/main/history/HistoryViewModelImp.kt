package com.example.mynotes.presentation.ui.screens.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import com.example.mynotes.domain.use_cases.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionGetHistory
import com.example.mynotes.presentation.utils.contstants.HISTORY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImp @Inject constructor(
    private val direction: HistoryDirection,
    private val currencyUseCase: CurrencyUseCases,
    private val historyUseCase: TransactionGetHistory,
    private val pocketUseCases: PocketUseCases,
    private val personsUseCases: PersonUseCases
) : ViewModel(), HistoryViewModel {

    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.invoke())
    }.take(HISTORY_LIMIT)

//    private val persons: Flow<List<PersonDomain>> = flow {
//        emitAll(personsUseCases.getAll.invoke())
//    }
//    private val pockets: Flow<List<PocketDomain>> = flow {
//        emitAll(pocketUseCases.getAll.invoke())
//    }
//    private val currencies: Flow<List<CurrencyDomain>> = flow {
//        emitAll(currencyUseCase.getAll.invoke())
//    }
//    private val transactions: Flow<List<TransactionDomain>> = flow {
//        emitAll(historyUseCase.invoke())
//    }

//    override val historyList: Flow<List<HistoryDomain>> = flow {
//        combine(
//            transactions,
//            persons,
//            pockets,
//            currencies
//        ) { transactionList, personList, pocketList, currencyList ->
//
//            val list = mutableListOf<HistoryDomain>()
//            transactionList.forEach { trans ->
//                val cur = currencyList.firstOrNull { it.id == trans.currencyId }!!
//                var nameFrom = ""
//                if (trans.fromId.isNotEmpty()) {
//                    val from = pocketList.firstOrNull { it.id == trans.fromId }
//                    nameFrom =
//                        if (from != null) from.name + " hamyondan" else personList.firstOrNull { it.id == trans.fromId }?.name + "dan"
//                }
//                var nameTo = ""
//                if (trans.toId.isNotEmpty()) {
//                    val to = pocketList.firstOrNull { it.id == trans.toId }
//                    nameTo =
//                        if (to != null) to.name + " hamyonga" else personList.firstOrNull() { it.id == trans.toId }?.name + "ga"
//                }
//                var moneyFrom = ""
//                var moneyTo = ""
//                if (getTypeEnum(trans.type) == Type.CONVERTATION) {
//                    val dollarAmount = (1 / cur.rate) * trans.amount
//                    val curFrom = currencyList.firstOrNull { it.id == trans.currencyFrom }!!
//                    val curTo = currencyList.firstOrNull { it.id == trans.currencyTo }!!
//                    moneyFrom = "-${curFrom.rate * dollarAmount} ${curFrom.name}"
//                    moneyTo = "+${curTo.rate * dollarAmount} ${curTo.name}"
//                }
//
//                list.add(
//                    HistoryDomain(
//                        title = getTypeEnum(trans.type),
//                        amount = trans.amount,
//                        currency = cur.name,
//                        from = nameFrom,
//                        to = nameTo,
//                        moneyFrom = moneyFrom,
//                        moneyTo = moneyTo,
//                        comment = trans.comment,
//                        date = trans.date,
//                    )
//                )
//            }
//            emit(list)
//        }//.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
//            .collect()
//    }

    override fun back() {
        viewModelScope.launch { direction.back() }
    }

}