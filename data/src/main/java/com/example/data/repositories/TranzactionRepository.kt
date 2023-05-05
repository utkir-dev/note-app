package com.example.mynotes.data.repositories

interface TranzactionRepository {
    suspend fun signIn(login: String, password: String)
    suspend fun signUp()
}