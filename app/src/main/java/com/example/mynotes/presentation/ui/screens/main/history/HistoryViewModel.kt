package com.example.mynotes.presentation.ui.screens.main.history

import com.example.mynotes.domain.models.HistoryDomain
import kotlinx.coroutines.flow.Flow


interface HistoryViewModel {
    fun back()
    fun checkNotUploadeds()

    // val historyList: Flow<List<HistoryItem>>
    val history: Flow<List<HistoryDomain>>
}