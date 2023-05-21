package com.example.mynotes.domain.use_cases.person_use_case

import com.example.data.repositories.intrefaces.PersonRepository
import javax.inject.Inject

class PersonGet @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke(id: String) =
        repository.get(id).toDomain()
}