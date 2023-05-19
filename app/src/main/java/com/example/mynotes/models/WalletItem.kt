package com.example.mynotes.models

data class WalletItem(
    var currencyName: String = "",
    var balance: Double = 0.0,
    var date: Long = 0
)
