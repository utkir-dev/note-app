package com.example.mynotes.domain.use_cases.wallet_use_case

import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.WalletDomain
import javax.inject.Inject

class WalletDelete @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(wallet: WalletDomain) = repository.delete(wallet.toLocal())

}