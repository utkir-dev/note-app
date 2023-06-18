package com.example.mynotes.domain.use_cases.transaction_use_case

import com.example.data.repositories.intrefaces.TransactionRepository
import javax.inject.Inject

class TransactionDownload @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(count: Int) {
        repository.download(count)
    }
}