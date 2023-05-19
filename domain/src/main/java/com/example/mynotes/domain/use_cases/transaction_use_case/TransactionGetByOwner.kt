package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.repositories.intrefaces.TransactionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionGetByOwner @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: String) =
        repository.getByOwnerId(id).map { it.map { it.toDomain() } }
}