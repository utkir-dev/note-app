package com.example.mynotes.domain.use_cases.device_use_case

import com.example.data.repositories.intrefaces.RemoteRepository
import javax.inject.Inject

class GetLocalDeviceId @Inject constructor(
    private val repository: RemoteRepository
) {
    suspend operator fun invoke() = repository.getLocalDeviceId()
}