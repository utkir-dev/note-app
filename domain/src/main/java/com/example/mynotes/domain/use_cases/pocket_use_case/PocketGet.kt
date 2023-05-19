package com.example.mynotes.domain.use_cases.pocket_use_case

import com.example.data.repositories.intrefaces.PocketRepository
import javax.inject.Inject

class PocketGet @Inject constructor(
    private val repository: PocketRepository
) {
    suspend operator fun invoke(id: String) =
        repository.get(id).toDomain()
}