package com.example.data.repositories.intrefaces


interface SharedPrefRepository {
    suspend fun saveLong(key: String, value: Long)
    suspend fun getLong(key: String): Long
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean
    suspend fun clearCash(): Boolean

}