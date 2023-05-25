package com.example.data.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.db.entities.Person
import com.example.data.db.entities.Wallet

data class PersonWithWallets(
    @Embedded val person: Person,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val wallets: List<Wallet>
)