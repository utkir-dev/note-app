package com.example.mynotes.domain.models

data class WalletDomain(
    val id: String,
    val ownerId: String,
    val currencyId: String,
    var balance: Double,
    var date: Long
)
