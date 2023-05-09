package com.example.data.repositories

import com.example.data.db.entities.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun add(currency: Currency): Long
    suspend fun update(currency: Currency): Int
    suspend fun delete(currency: Currency): Int
    suspend fun get(name: String): Flow<Currency>
    suspend fun getAll(): Flow<List<Currency>>
}