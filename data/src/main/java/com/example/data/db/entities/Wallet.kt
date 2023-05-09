package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey
    val id: String,
    var ownerId: String,
    var currencyId: String,
    var balance: Double,
    var date: Long,
    var uploaded: Boolean = false
)
