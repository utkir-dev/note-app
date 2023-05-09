package com.example.data.db.remote_models

import com.example.data.db.entities.Currency

data class CurrencyRemote(
    val id: String,
    var name: String = "",
    var rate: Double = 0.0,
    var date: Long = 0
) {
    fun toCurrency() = Currency(
        id = this.id,
        name = this.name,
        rate = this.rate,
        date = this.date,
        uploaded = true,
    )
}

