package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Pocket
import kotlinx.coroutines.flow.Flow

interface PocketRepository {
    suspend fun add(pocket: Pocket): Long
    suspend fun update(pocket: Pocket): Int
    suspend fun delete(pocket: Pocket): Int
    suspend fun get(name: String): Pocket
    suspend fun getAll(): Flow<List<Pocket>>
}