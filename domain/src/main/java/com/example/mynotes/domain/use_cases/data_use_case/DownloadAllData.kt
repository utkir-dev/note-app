package com.example.mynotes.domain.use_cases.data_use_case

import com.example.common.ResponseResult
import com.example.data.repositories.intrefaces.RemoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadAllData @Inject constructor(
    private val repository: RemoteRepository
) {
    suspend operator fun invoke(): Flow<Boolean> =
        repository.downloadAllData()

}