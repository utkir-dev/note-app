package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.db.entities.Currency
import com.example.data.repositories.intrefaces.CurrencyRepository
import com.example.mynotes.domain.models.CurrencyDomain
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrencyGetById @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(id: String): CurrencyDomain =
        repository.getById(id).toDomain()


    fun Currency.toDomain() = CurrencyDomain(
        id = this.id,
        name = this.name,
        rate = this.rate,
        date = this.date,
        uploaded = this.uploaded
    )
}