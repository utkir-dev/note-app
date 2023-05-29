package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.db.remote_models.TransactionRemote

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
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
    var uploaded: Boolean = false,

    var isFromPocket: Boolean = false,
    var isToPocket: Boolean = false,
    var rate: Double = 1.0,
    var rateFrom: Double = 1.0,
    var rateTo: Double = 1.0,
    var balance: Double = 0.0
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

    fun toRemote() = TransactionRemote(
        id = this.id,
        type = this.type,
        fromId = this.fromId,
        toId = this.toId,
        currencyId = this.currencyId,
        amount = this.amount,
        currencyFrom = this.currencyFrom,
        currencyTo = this.currencyTo,
        date = this.date,
        comment = this.comment,

        isFromPocket = this.isFromPocket,
        isToPocket = this.isToPocket,
        rate = this.rate,
        rateFrom = this.rateFrom,
        rateTo = this.rateTo,
        balance = this.balance,
    )
}
