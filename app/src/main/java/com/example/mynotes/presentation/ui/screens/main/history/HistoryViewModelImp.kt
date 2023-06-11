package com.example.mynotes.presentation.ui.screens.main.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImp @Inject constructor(
    private val direction: HistoryDirection,
    private val historyUseCase: TransactionUseCases,
    private val dataUseCases: DataUseCases,
) : ViewModel(), HistoryViewModel {

    override fun back() {
        viewModelScope.launch { direction.back() }
    }

    override fun checkNotUploadeds() {
        viewModelScope.launch {
            dataUseCases.checkAllData.invoke()
        }
    }


// pagination

    private val LIMIT = 10
    val historyListForPaging = mutableStateListOf<HistoryDomain>()
    private var page by mutableStateOf(0)
    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)

    init {
        getHistoryList()
    }

    fun getHistoryList() = viewModelScope.launch {
        if (page == 0 || (page != 0 && canPaginate) && listState == ListState.IDLE) {
            listState = if (page == 0) ListState.LOADING else ListState.PAGINATING

            historyUseCase.getHistoryForPaging.invoke(LIMIT, page * LIMIT).collect() { list ->
                if (list.isNotEmpty()) {
                    canPaginate = list.size == LIMIT

                    if (page == 0) {
                        historyListForPaging.clear()
                        historyListForPaging.addAll(list)
                    } else {
                        historyListForPaging.addAll(list)
                    }
                    listState = ListState.IDLE

                    if (canPaginate)
                        page++
                } else {
                    listState = if (page == 0) ListState.ERROR else ListState.PAGINATION_EXHAUST
                }
            }
        }
    }

    override fun onCleared() {
        page = 1
        listState = ListState.IDLE
        canPaginate = false
        super.onCleared()
    }

}