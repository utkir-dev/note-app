package com.example.mynotes.domain.use_cases.transaction_use_case

import android.util.Log
import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.data.repositories.intrefaces.WalletRepository
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.TransactionDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
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
    ) {
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
                balance = amountDollar * curTo.rate,
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