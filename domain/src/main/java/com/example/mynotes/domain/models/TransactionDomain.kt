package com.example.mynotes.domain.models

import com.example.data.db.entities.Transaction

data class TransactionDomain(
    var id: String,
    var type: Int = 0,
    var fromId: String,
    var toId: String,
    var currencyId: String,
    var amount: Double,
    var date: Long,
    var comment: String = "",
) {
    fun toLocal() = Transaction(
        id = this.id,
        type = this.type,
        fromId = this.fromId,
        toId = this.toId,
        currencyId = this.currencyId,
        amount = this.amount,
        date = this.date,
        comment = this.comment
    )

    fun isValid(): Boolean =
        id.trim().isNotEmpty() &&
                (fromId.trim().isNotEmpty() || toId.trim().isNotEmpty()) &&
                currencyId.trim().isNotEmpty() &&
                amount > 0

}
