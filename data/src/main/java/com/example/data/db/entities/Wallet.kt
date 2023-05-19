package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.db.remote_models.WalletRemote

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey
    val id: String,
    var type: Int = 0,
    var ownerId: String,
    var currencyId: String,
    var balance: Double,
    var date: Long,
    var uploaded: Boolean = false
) {
    fun toRemote() = WalletRemote(
        id = this.id,
        type = this.type,
        ownerId = this.ownerId,
        currencyId = this.currencyId,
        balance = this.balance,
        date = this.date,
    )
}
