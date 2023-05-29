package com.example.data.db.database_relations

import androidx.room.Entity

@Entity
data class Balance(
    var name: String,
    var amount: Double,
    var rate: Double,
)
