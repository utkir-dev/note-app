package com.example.mynotes.domain.models

import com.example.data.db.entities.Transaction

data class TransactionDomain(
    var id: String,
    var type: Int = 0,
    var fromId: String,
    var toId: String,
    var currencyId: String,
    var amount: Double,
    var currencyFrom: String = "",
    var currencyTo: String = "",
    var date: Long,
    var comment: String = "",
) {
    override fun toString(): String {
        return """
            id = $id
            type = $type
            fromId = $fromId
            toId = $toId
            currencyId = $currencyId
            amount = $amount
            currencyFrom = $currencyFrom
            currencyTo = $currencyTo
            date = $date
            comment = $comment
        """.trimIndent()
    }

    fun toLocal() = Transaction(
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

    fun isValid(): Boolean =
        id.trim().isNotEmpty() &&
                (fromId.trim().isNotEmpty() || toId.trim().isNotEmpty()) &&
                currencyId.trim().isNotEmpty() &&
                amount > 0

}
