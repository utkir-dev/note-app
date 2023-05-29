package com.example.mynotes.presentation.ui.screens.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
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
    private val historyUseCase: TransactionUseCases,
) : ViewModel(), HistoryViewModel {

    val history: Flow<List<HistoryDomain>> = flow {
        emitAll(historyUseCase.getHistory.invoke())
    }
    override fun back() {
        viewModelScope.launch { direction.back() }
    }

}