package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.db.OwnerI
import com.example.data.db.remote_models.PocketRemote

@Entity(tableName = "pockets")
data class Pocket(
    @PrimaryKey
    val id: String,
    var name: String,
    var date: Long,
    var uploaded: Boolean = false

) : OwnerI {
    fun toRemote() = PocketRemote(
        id = this.id,
        name = this.name,
        date = this.date
    )
}
