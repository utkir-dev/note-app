package com.example.mynotes.domain.use_cases.auth_use_case

import com.example.data.repositories.intrefaces.AuthRepository
import javax.inject.Inject

class CheckUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.currentUser

}