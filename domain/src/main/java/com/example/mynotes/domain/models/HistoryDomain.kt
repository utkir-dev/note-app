package com.example.mynotes.domain.models

data class HistoryDomain(
    var title: Int = 0,
    var amount: Double = 0.0,
    var currency: String = "",
    var fromName: String? = null,
    var toName: String? = null,
    var moneyFrom: String? = null,
    var moneyTo: String? = null,
    var comment: String? = null,
    var date: Long = 0,
)
