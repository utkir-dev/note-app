package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Person
import com.example.data.db.entities.Pocket
import com.example.data.db.models.PersonWithWallets
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun add(person: Person): Long
    suspend fun update(person: Person): Int
    suspend fun delete(person: Person): Int
    suspend fun get(name: String): Person
    suspend fun getAll(): Flow<List<Person>>
    suspend fun getPersonWithWallets(): Flow<List<PersonWithWallets>>
}