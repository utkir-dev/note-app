package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.db.models.History
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.mynotes.domain.models.HistoryDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionGetHistory @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke() =
        repository.getHistory().map { it.map { it.toDomain() } }
}

fun History.toDomain() = HistoryDomain(
    title = this.title,
    amount = this.amount,
    currency = this.currency,
    fromName = this.fromName,
    toName = this.toName,
    moneyFrom = this.moneyFrom,
    moneyTo = this.moneyTo,
    comment = this.comment,
    date = this.date
)

