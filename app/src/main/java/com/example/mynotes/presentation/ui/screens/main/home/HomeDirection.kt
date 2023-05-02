package com.example.mynotes.presentation.ui.screens.main.home

interface HomeDirection {
    suspend fun navigateToBalance()
    suspend fun navigateToIncome()
    suspend fun navigateToOutIncome()
    suspend fun navigateToGetCredit()
    suspend fun navigateToGiveCredit()
    suspend fun navigateToCreditors()
    suspend fun navigateToDebetors()
    suspend fun navigateToPockets()
    suspend fun navigateToCurrencies()
    suspend fun navigateToHistory()
}