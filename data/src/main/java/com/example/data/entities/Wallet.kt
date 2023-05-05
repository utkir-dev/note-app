package com.example.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val currencyId: String,
    var balance: Double,
    var date: Long,
    var uploaded: Boolean = false
)
