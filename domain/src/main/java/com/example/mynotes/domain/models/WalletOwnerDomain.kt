package com.example.mynotes.domain.models

data class WalletOwnerDomain(
    var id: String,
    var ownerId: String,
    var currencyName: String,
    var currencyBalance: Double = 0.0,
    var rate: Double = 1.0
)
