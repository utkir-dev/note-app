package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.db.entities.Transaction
import com.example.data.repositories.intrefaces.TransactionRepository
import com.example.mynotes.domain.models.TransactionDomain
import com.example.mynotes.domain.use_cases.currency_use_case.toDomain
import com.example.mynotes.domain.use_cases.pocket_use_case.toDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionGetAll @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke() =
        repository.getAll().map { it.map { it.toDomain() } }
}

fun Transaction.toDomain() = TransactionDomain(
    id = this.id,
    type = this.type,
    fromId = this.fromId,
    toId = this.toId,
    currencyId = this.currencyId,
    amount = this.amount,
    currencyFrom = this.currencyFrom,
    currencyTo = this.currencyTo,
    date = this.date,
    comment = this.comment
)

