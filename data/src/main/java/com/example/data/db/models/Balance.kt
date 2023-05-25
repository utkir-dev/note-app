package com.example.data.db.models

import androidx.room.Entity

@Entity
data class Balance(
    var name: String,
    var amount: Double,
    var rate: Double,
)
