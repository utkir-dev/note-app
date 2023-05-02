package com.example.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey
    val id: String,
    var name: String,
    var rate: Double,
    var date: Long,
    var uploaded: Boolean = false
)
