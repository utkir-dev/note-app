package com.example.mynotes.domain.use_cases.person_use_case

import com.example.data.repositories.intrefaces.PersonRepository
import com.example.mynotes.domain.models.PersonDomain
import javax.inject.Inject

class PersonUpdate @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke(person: PersonDomain) =
        repository.update(person.toLocal())
}