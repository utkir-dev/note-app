package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Transaction
import com.example.data.db.models.History
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun add(transaction: Transaction): Long
    suspend fun delete(transaction: Transaction): Int
    suspend fun getForHome(count: Int): Flow<List<Transaction>>
    suspend fun getByOwnerId(id: String): Flow<List<Transaction>>
    suspend fun getAll(): Flow<List<Transaction>>
    suspend fun getHistory(): Flow<List<History>>
}