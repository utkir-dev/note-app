package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.data.db.remote_models.CurrencyRemote

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey

    val id: String,
    var name: String,
    var rate: Double,
    var date: Long,
    var uploaded: Boolean = false
) {
    fun toRemote() = CurrencyRemote(
        id = this.id,
        name = this.name,
        rate = this.rate,
        date = this.date
    )
}
