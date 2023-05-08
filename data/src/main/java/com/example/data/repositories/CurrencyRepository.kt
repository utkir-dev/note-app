package com.example.data.repositories

import com.example.data.entities.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun add(name: String, rate: Double): Long
    suspend fun update(currency: Currency): Int
    suspend fun delete(currency: Currency): Int
    suspend fun get(name: String): Flow<Currency>
    suspend fun getAll(): Flow<List<Currency>>
}