package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.WalletOwnerDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWalletsByOwnerId @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(id: String): Flow<List<WalletOwnerDomain>> =
        repository.getWalletsByOwnerId(id).map { it.map { it.toDomain() } }

}

