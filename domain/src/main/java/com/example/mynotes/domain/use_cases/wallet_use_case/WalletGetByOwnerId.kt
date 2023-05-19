package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletGetByOwnerId @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(ownerId: String): Flow<List<WalletDomain>> =
        repository.getByOwnerId(ownerId).map { it.map { it.toDomain() } }

}

