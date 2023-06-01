package com.example.mynotes.domain.use_cases.data_use_case

import com.example.data.repositories.intrefaces.RemoteRepository
import javax.inject.Inject

class ObserveDevice @Inject constructor(
    private val repository: RemoteRepository
) {
    suspend operator fun invoke() = repository.observeDevice()
}