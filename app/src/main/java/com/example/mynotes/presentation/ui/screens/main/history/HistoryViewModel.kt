package com.example.mynotes.presentation.ui.screens.main.history

import com.example.mynotes.models.HistoryItem
import kotlinx.coroutines.flow.Flow


interface HistoryViewModel {
    fun back()
    val historyList: Flow<List<HistoryItem>>
}