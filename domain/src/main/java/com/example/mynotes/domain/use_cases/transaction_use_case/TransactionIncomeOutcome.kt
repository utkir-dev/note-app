package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.TransactionDomain
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject

class TransactionIncomeOutcome @Inject constructor(
    private val repository: TransactionRepository,
    private val repWallet: WalletRepository
) {
    suspend operator fun invoke(trans: TransactionDomain) {
        val fromWallet = repWallet.getByOwnerAndCurrencyId(trans.fromId, trans.currencyId)
        val toWallet = repWallet.getByOwnerAndCurrencyId(trans.toId, trans.currencyId)

        // kirim
        if (trans.toId.isNotEmpty() && trans.fromId.isEmpty()) {
            transactionTo(toWallet, trans)
        } else
        // chiqim
            if (trans.fromId.isNotEmpty() && trans.toId.isEmpty()) {
                if (fromWallet.balance >= trans.amount) {
                    transactionFrom(fromWallet, trans)
                }
            }
    }

    private suspend fun transactionFrom(
        wallet: Wallet,
        trans: TransactionDomain
    ) {
        val newWallet = wallet.copy(
            balance = wallet.balance - trans.amount, date = trans.date
        )
        runBlocking {
            val def = async {
                repWallet.add(newWallet)
            }
            val result: Long = def.await()
            if (result > 0) {
                repository.add(trans.toLocal())
            }
        }
    }

    private suspend fun transactionTo(
        wallet: Wallet, trans: TransactionDomain
    ) {
        val newWallet = if (wallet == null) {
            Wallet(
                id = UUID.randomUUID().toString(),
                ownerId = trans.toId,
                currencyId = trans.currencyId,
                balance = trans.amount,
                date = trans.date,

                )
        } else {
            wallet.copy(
                balance = wallet.balance + trans.amount, date = trans.date
            )
        }
        runBlocking {
            val def = async {
                repWallet.add(newWallet)
            }
            val result: Long = def.await()
            if (result > 0) {
                repository.add(trans.toLocal())
            }
        }
    }
}