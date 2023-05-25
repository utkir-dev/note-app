package com.example.data.db.models

import androidx.room.Entity

@Entity
data class WalletOwner(
    var id: String,
    var ownerId: String,
    var currencyName: String,
    var currencyBalance: Double,
    var rate: Double
)
