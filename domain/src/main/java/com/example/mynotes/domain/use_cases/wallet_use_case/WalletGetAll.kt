package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletGetAll @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): Flow<List<WalletDomain>> =
        repository.getAll().map { it.map { it.toDomain() } }

}

fun Wallet.toDomain() = WalletDomain(
    id = this.id,
    ownerId = this.ownerId,
    currencyId = this.currencyId,
    balance = this.balance,
    date = this.date
)