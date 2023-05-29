package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.db.OwnerI
import com.example.data.db.remote_models.PersonRemote

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey
    val id: String,
    var name: String,
    var phone: String,
    var address: String,
    var date: Long,
    var uploaded: Boolean = false
) : OwnerI {
    fun toRemote() = PersonRemote(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        date = this.date
    )
}
