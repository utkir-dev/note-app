package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.repositories.CurrencyRepository
import javax.inject.Inject

class CurrencyAdd @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(name: String, rate: Double) = repository.add(name, rate)
}