package com.example.mynotes.domain.models

import com.example.data.db.entities.Pocket

data class PocketDomain(
    override val id: String,
    override var name: String = "",
    var date: Long = 0
) : ModelDomain {
    fun toLocal() = Pocket(
        id = this.id,
        name = this.name,
        date = this.date
    )

    fun isValid(): Boolean =
        id.trim().isNotEmpty() && name.trim().isNotEmpty() /*&& ownerId.isNotEmpty()*/

}
