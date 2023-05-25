package com.example.data.db.remote_models

import com.example.data.db.entities.Wallet

data class WalletRemote(
    val id: String,
    var type: Int = 0,
    val ownerId: String,
    val currencyId: String,
    var balance: Double = 0.0,
    var date: Long = 0
) {
    fun toLocal() = Wallet(
        id = this.id,
        type = this.type,
        ownerId = this.ownerId,
        currencyId = this.currencyId,
        balance = this.balance,
        date = this.date,
        uploaded = true,
    )
}

