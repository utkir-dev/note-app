package com.example.mynotes.domain.models

data class CurrencyRemote(
    val id: String,
    var name: String,
    var rate: Double,
    var date: Long
)
