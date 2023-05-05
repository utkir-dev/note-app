package com.example.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val id: String,
    var name: String,
    val model: String,
    var date: Long
)
