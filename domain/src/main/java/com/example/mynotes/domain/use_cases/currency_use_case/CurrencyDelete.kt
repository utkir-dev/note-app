package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.entities.Currency
import com.example.data.repositories.CurrencyRepository
import javax.inject.Inject

class CurrencyDelete @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(currency: Currency) =
        repository.delete(currency)
}