package com.example.mynotes.domain.use_cases.auth_use_case

import com.example.mynotes.data.repositories.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String)=
        repository.signUpWithEmailAndPassword(login, password)

}