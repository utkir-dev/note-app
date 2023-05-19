package com.example.mynotes.domain.models

import com.example.data.db.entities.Wallet

data class WalletDomain(
    val id: String,
    var ownerId: String = "",
    var currencyId: String = "",
    var balance: Double = 0.0,
    var date: Long = 0
) {
    fun toLocal() = Wallet(
        id = this.id,
        ownerId = this.ownerId,
        currencyId = this.currencyId,
        balance = this.balance,
        date = this.date
    )

    fun isValid(): Boolean =
        id.trim().isNotEmpty() && ownerId.trim().isNotEmpty() && currencyId.trim().isNotEmpty()

}
