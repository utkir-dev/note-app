package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.db.entities.database_relations.Balance
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.BalanceDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBalance @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): Flow<List<BalanceDomain>> =
        repository.getBalabce().map { it.map { it.toDomain() } }

}

fun Balance.toDomain() = BalanceDomain(
    currencyName = this.name,
    amount = this.amount,
    rate = this.rate
)