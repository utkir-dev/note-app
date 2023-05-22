package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.TransactionDomain
import com.example.mynotes.domain.models.WalletDomain
import java.util.*
import javax.inject.Inject

class TransactionConvertation @Inject constructor(
    private val repository: TransactionRepository,
    private val repWallet: WalletRepository
) {
    suspend operator fun invoke(
        trans: TransactionDomain,
        currencyConvert: CurrencyDomain,
        fromWallet: WalletDomain,
        curFrom: CurrencyDomain,
        curTo: CurrencyDomain,
        amountDollar: Double
    ): Long {
//from
        val newWalletFrom = if (fromWallet.currencyId == currencyConvert.id) {
            fromWallet.copy(
                balance = fromWallet.balance - trans.amount,
                date = trans.date
            )
        } else {
            val balanceDollar = fromWallet.balance / curFrom.rate
            fromWallet.copy(
                balance = (balanceDollar - amountDollar) * curFrom.rate,
                date = trans.date
            )
        }.toLocal()
// to
        val toWallet = repWallet.getByOwnerAndCurrencyId(trans.toId, curTo.id)
        val newWalletTo = if (toWallet == null) {
            Wallet(
                id = UUID.randomUUID().toString(),
                ownerId = trans.toId,
                currencyId = curTo.id,
                balance = trans.amount,
                date = trans.date,
            )
        } else if (toWallet.currencyId == currencyConvert.id) {
            toWallet.copy(
                balance = toWallet.balance + trans.amount,
                date = trans.date
            )
        } else {
            val balanceDollar = toWallet.balance / curTo.rate
            toWallet.copy(
                balance = (balanceDollar + amountDollar) * curTo.rate,
                date = trans.date
            )
        }

        repWallet.add(newWalletFrom)
        repWallet.add(newWalletTo)

        return repository.add(trans.toLocal())
    }


}