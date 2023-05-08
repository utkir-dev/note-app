package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey
    val id: String,
    var name: String,
    var surname: String,
    var address: String,
    var date: Long,
    var uploaded: Boolean = false
)
