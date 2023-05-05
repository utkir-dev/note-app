package com.example.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pockets")
data class Pocket(
    @PrimaryKey
    val id: String,
    val personId: String,
    var name: String,
    var date: Long,
    var uploaded: Boolean = false
)
