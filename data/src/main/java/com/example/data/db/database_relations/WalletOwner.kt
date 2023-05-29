package com.example.data.db.database_relations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WalletOwner(
    @PrimaryKey
    var id: String,
    var ownerId: String,
    var currencyName: String,
    var currencyBalance: Double,
    var rate: Double
)
