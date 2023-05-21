package com.example.mynotes.domain.use_cases.person_use_case

import com.example.data.db.entities.Person
import com.example.data.repositories.intrefaces.PersonRepository
import com.example.mynotes.domain.models.PersonDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonGetAll @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke(): Flow<List<PersonDomain>> =
        repository.getAll().map { it.map { it.toDomain() } }

}

fun Person.toDomain() = PersonDomain(
    id = this.id,
    name = this.name,
    phone = this.phone,
    address = this.address,
    date = this.date
)