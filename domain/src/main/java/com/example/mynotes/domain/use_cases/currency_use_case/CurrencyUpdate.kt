package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.repositories.intrefaces.CurrencyRepository
import com.example.mynotes.domain.models.CurrencyDomain
import javax.inject.Inject

class CurrencyUpdate @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(currency: CurrencyDomain) =
        repository.update(currency.toCurrency())
}