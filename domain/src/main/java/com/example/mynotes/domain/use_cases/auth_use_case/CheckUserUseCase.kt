package com.example.mynotes.domain.use_cases.auth_use_case

import com.example.data.repositories.AuthRepository
import javax.inject.Inject

class CheckUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.currentUser

}