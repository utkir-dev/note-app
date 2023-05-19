package com.example.mynotes.domain.use_cases.pocket_use_case

import com.example.data.repositories.intrefaces.PocketRepository
import com.example.mynotes.domain.models.PocketDomain
import javax.inject.Inject

class PocketDelete @Inject constructor(
    private val repository: PocketRepository
) {
    suspend operator fun invoke(pocket: PocketDomain) = repository.delete(pocket.toLocal())

}