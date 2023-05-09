package com.example.data.db.remote_models

data class WalletRemote(
    val id: String,
    val ownerId: String,
    val currencyId: String,
    var balance: Double,
    var date: Long
)
