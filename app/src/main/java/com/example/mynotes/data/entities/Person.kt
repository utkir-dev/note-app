package com.example.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey
    val id: String,
    val name: String,
    val surname: String,
    var address: String,
    var date: Long,
    var uploaded: Boolean = false
)
