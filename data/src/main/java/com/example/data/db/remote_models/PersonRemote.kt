package com.example.data.db.remote_models

import com.example.data.db.entities.Person

data class PersonRemote(
    var id: String = "",
    var name: String = "",
    var phone: String = "",
    var address: String = "",
    var date: Long = 0
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
