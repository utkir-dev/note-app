package com.example.mynotes.domain.models

import com.example.data.db.entities.Currency

data class CurrencyDomain(
    override val id: String,
    override var name: String = "",
    var rate: Double = 0.0,
    var date: Long = 0,
    var uploaded: Boolean = false
) : ModelDomain {
    fun toCurrency() = Currency(
        id = this.id,
        name = this.name,
        rate = this.rate,
        date = this.date
    )

    fun isValid(): Boolean = id.trim().isNotEmpty() && name.trim().isNotEmpty() && rate > 0
}

