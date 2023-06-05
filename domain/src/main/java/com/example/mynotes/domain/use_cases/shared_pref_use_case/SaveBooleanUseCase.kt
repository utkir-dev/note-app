package com.example.mynotes.domain.use_cases.shared_pref_use_case

import com.example.data.repositories.intrefaces.SharedPrefRepository
import javax.inject.Inject

class SaveBooleanUseCase @Inject constructor(
    private val repository: SharedPrefRepository
) {
    suspend operator fun invoke(key: String, value: Boolean) {
        repository.saveBoolean(key, value)
    }
}