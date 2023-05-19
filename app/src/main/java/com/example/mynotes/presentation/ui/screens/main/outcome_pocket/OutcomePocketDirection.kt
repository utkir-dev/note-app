package com.example.mynotes.presentation.ui.screens.main.outcome_pocket

interface OutcomePocketDirection {
    suspend fun back()
    suspend fun navigateToCurrency(pocketId: String)
}