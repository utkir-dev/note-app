package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screen_block")
data class ScreenBlock(
    @PrimaryKey
    var id: String = "",
    var pincode: String = "",
    var date: Long = 0,
    var access: Boolean = false,
)
