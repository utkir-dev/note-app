package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.repositories.intrefaces.TransactionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionGetForPaging @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(count: Int, page: Int) =
        repository.getHistory(count, page).map { it.map { it.toDomain() } }
}