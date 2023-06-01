package com.example.mynotes.presentation.ui.screens.main.home

interface HomeDirection {
    suspend fun navigateToBalance()
    suspend fun navigateToIncome()
    suspend fun navigateToOutcome()
    suspend fun navigateToGetCredit()
    suspend fun navigateToGiveCredit()
    suspend fun navigateToPersons()
    suspend fun navigateToConvertation()
    suspend fun navigateToPockets()
    suspend fun navigateToCurrencies()
    suspend fun navigateToHistory()
    suspend fun replaceToSignIn()
}