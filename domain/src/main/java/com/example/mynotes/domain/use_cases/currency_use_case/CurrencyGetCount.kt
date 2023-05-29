package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.repositories.intrefaces.CurrencyRepository
import javax.inject.Inject

class CurrencyGetCount @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke() = repository.getCount()
}

