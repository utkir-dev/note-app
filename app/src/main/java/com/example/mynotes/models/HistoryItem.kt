package com.example.mynotes.models

data class HistoryItem(
    var title: String = "",
    var amount: Double = 0.0,
    var currency: String = "",
    var from: String = "",
    var to: String = "",
    var moneyFrom: String = "",
    var moneyTo: String = "",
    var comment: String = "",
    var date: Long = 0,
)
