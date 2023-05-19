package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.repositories.intrefaces.WalletRepository
import javax.inject.Inject

class WalletGet @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(ownerId: String, currencyId: String) =
        repository.getByOwnerAndCurrencyId(ownerId, currencyId)

    suspend operator fun invoke(ownerId: String) =
        repository.getByOwnerId(ownerId)
}