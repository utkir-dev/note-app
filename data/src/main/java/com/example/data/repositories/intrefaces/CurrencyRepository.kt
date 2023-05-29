package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun add(currency: Currency): Long
    suspend fun update(currency: Currency): Int
    suspend fun delete(currency: Currency): Int
    suspend fun getById(id: String): Currency
    suspend fun getByWalletIds(ids: List<String>): Flow<List<Currency>>
    suspend fun getAll(): Flow<List<Currency>>
    suspend fun getCount(): Int
}