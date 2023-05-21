package com.example.data.db.remote_models

import com.example.data.db.entities.Person

data class PersonRemote(
    val id: String,
    val name: String,
    val phone: String,
    var address: String,
    var date: Long
) {
    fun toLocal() = Person(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        date = this.date,
        uploaded = true,
    )
}
