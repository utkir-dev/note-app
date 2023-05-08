package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tranzactions")
data class Tranzaction(
    @PrimaryKey
    val id: String,
    var fromId: String,
    var toId: String,
    var currencyId: String,
    var amount: Double,
    var date: Long,
    var comment: String = "",
    var uploaded: Boolean = false
)
