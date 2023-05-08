package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.repositories.CurrencyRepository
import javax.inject.Inject

class CurrencyGet @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(name: String) =
        repository.get(name)
}