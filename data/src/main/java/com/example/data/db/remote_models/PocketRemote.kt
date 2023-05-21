package com.example.data.db.remote_models

import com.example.data.db.entities.Person
import com.example.data.db.entities.Pocket

data class PocketRemote(
    val id: String,
    var name: String,
    var date: Long
) {
    fun toLocal() = Pocket(
        id = this.id,
        name = this.name,
        date = this.date,
        uploaded = true,
    )
}
