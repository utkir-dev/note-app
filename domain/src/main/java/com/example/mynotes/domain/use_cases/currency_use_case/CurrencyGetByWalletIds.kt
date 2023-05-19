package com.example.mynotes.domain.use_cases.currency_use_case

import com.example.data.repositories.intrefaces.CurrencyRepository
import com.example.mynotes.domain.use_cases.wallet_use_case.toDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyGetByWalletIds @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(ids: List<String>) =
        repository.getByWalletIds(ids).map { it.map { it.toDomain() } }
}