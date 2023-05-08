package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.entities.Currency
import com.example.data.repositories.CurrencyRepository
import com.example.mynotes.domain.models.CurrencyRemote
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrencyGetAll @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Flow<List<CurrencyRemote>> =
        repository.getAll().map { it.map { it.toRemote() } }

}

fun Currency.toRemote() = CurrencyRemote(
    id = this.id,
    name = this.name,
    rate = this.rate,
    date = this.date,
)