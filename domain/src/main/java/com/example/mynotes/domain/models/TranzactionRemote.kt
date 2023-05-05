package com.example.mynotes.domain.models

data class TranzactionRemote(
    val id: String,
    var fromId: String,
    var toId: String,
    var currencyId: String,
    var amount: Double,
    var date: Long,
    var comment: String = "",
)
