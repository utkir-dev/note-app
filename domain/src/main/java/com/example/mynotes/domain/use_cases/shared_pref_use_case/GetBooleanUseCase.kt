package com.example.mynotes.domain.use_cases.shared_pref_use_case

import com.example.data.repositories.intrefaces.SharedPrefRepository
import javax.inject.Inject

class GetBooleanUseCase @Inject constructor(
    private val repository: SharedPrefRepository
) {
    suspend operator fun invoke(key: String) =
        repository.getBoolean(key)

}