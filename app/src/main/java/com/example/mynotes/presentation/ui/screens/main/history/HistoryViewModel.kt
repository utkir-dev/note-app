package com.example.mynotes.presentation.ui.screens.main.history

import kotlinx.coroutines.flow.Flow


interface HistoryViewModel {
    fun back()
    fun checkNotUploadeds()
    val historyCount: Flow<Int>

}