package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Transaction
import com.example.data.db.entities.database_relations.History
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun add(transaction: Transaction): Long
    suspend fun delete(transaction: Transaction): Int
    suspend fun getForHome(count: Int): Flow<List<History>>
    suspend fun getAll(): Flow<List<Transaction>>
    suspend fun getHistory(): Flow<List<History>>
    suspend fun getByOwnerId(ownerId: String): Flow<List<History>>
}