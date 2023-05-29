package com.example.mynotes.domain.use_cases.transaction_use_case

import android.util.Log
import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.TransactionDomain
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject

class TransactionDebetCredit @Inject constructor(
    private val repository: TransactionRepository,
    private val repWallet: WalletRepository
) {
    suspend operator fun invoke(trans: TransactionDomain) {
        val fromWallet = repWallet.getByOwnerAndCurrencyId(trans.fromId, trans.currencyId)
        val toWallet = repWallet.getByOwnerAndCurrencyId(trans.toId, trans.currencyId)
        val newWalletFrom = if (fromWallet == null) {
            Wallet(
                id = UUID.randomUUID().toString(),
                ownerId = trans.fromId,
                currencyId = trans.currencyId,
                balance = -trans.amount,
                date = trans.date,

                )
        } else {
            fromWallet.copy(
                balance = fromWallet.balance - trans.amount, date = trans.date
            )
        }
        val newWalletTo = if (toWallet == null) {
            Wallet(
                id = UUID.randomUUID().toString(),
                ownerId = trans.toId,
                currencyId = trans.currencyId,
                balance = trans.amount,
                date = trans.date,
            )
        } else {
            toWallet.copy(
                balance = toWallet.balance + trans.amount, date = trans.date
            )
        }
        runBlocking {
            val def1 = async {
                repWallet.addWallets(listOf(newWalletFrom, newWalletTo))
            }
            val result: List<Long> = def1.await()
            if (result.isNotEmpty()) {
                repository.add(trans.toLocal())
            }
        }
    }
}