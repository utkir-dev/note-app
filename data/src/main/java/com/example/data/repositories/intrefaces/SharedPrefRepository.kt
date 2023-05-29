package com.example.data.repositories.intrefaces


interface SharedPrefRepository {
    suspend fun saveLong(key: String, value: Long)
    suspend fun getLong(key: String): Long
}