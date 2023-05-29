package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.db.database_relations.WalletOwner
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.WalletOwnerDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWalletsByOwnes @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): Flow<List<WalletOwnerDomain>> =
        repository.getWalletsByOwnes().map { it.map { it.toDomain() } }

}

fun WalletOwner.toDomain() = WalletOwnerDomain(
    id = this.id,
    ownerId = this.ownerId,
    currencyName = this.currencyName,
    currencyBalance = this.currencyBalance,
    rate = this.rate
)