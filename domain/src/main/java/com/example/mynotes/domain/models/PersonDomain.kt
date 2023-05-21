package com.example.mynotes.domain.models

import com.example.data.db.entities.Person

data class PersonDomain(
    override val id: String,
    override var name: String = "",
    var phone: String = "",
    var address: String = "",
    var date: Long = 0
) : ModelDomain {
    fun toLocal() = Person(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        date = this.date,
    )

    fun isValid(): Boolean =
        id.trim().isNotEmpty() && name.trim().isNotEmpty() /*&& ownerId.isNotEmpty()*/

}
