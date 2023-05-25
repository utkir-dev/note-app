package com.example.data.db.remote_models

import com.example.data.db.entities.Transaction

data class TransactionRemote(
    val id: String,
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
}
