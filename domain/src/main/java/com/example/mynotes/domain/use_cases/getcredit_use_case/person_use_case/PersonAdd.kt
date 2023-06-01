package com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case

import com.example.data.repositories.intrefaces.PersonRepository
import com.example.mynotes.domain.models.PersonDomain
import javax.inject.Inject

class PersonAdd @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke(person: PersonDomain) =
        repository.add(person.toLocal())
}