package com.example.mynotes.domain.use_cases.pocket_use_case

import com.example.data.db.entities.Pocket
import com.example.data.repositories.intrefaces.PocketRepository
import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PocketGetAll @Inject constructor(
    private val repository: PocketRepository
) {
    suspend operator fun invoke(): Flow<List<PocketDomain>> =
        repository.getAll().map { it.map { it.toDomain() } }

}

fun Pocket.toDomain() = PocketDomain(
    id = this.id,
    name = this.name,
    date = this.date,
)