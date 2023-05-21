package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.TransactionDomain
import java.util.UUID
import javax.inject.Inject

class TransactionDebetCredit @Inject constructor(
    private val repository: TransactionRepository,
    private val repWallet: WalletRepository
) {
    suspend operator fun invoke(trans: TransactionDomain): Long {
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
        repWallet.add(newWalletFrom)
        repWallet.add(newWalletTo)

        return repository.add(trans.toLocal())
    }


}