package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun add(wallet: Wallet): Long
    suspend fun update(wallet: Wallet): Int
    suspend fun delete(wallet: Wallet): Int
    suspend fun getByOwnerAndCurrencyId(ownerId: String, currencyId: String): Wallet
    suspend fun getByOwnerId(ownerId: String): Flow<List<Wallet>>
    suspend fun getAll(): Flow<List<Wallet>>
}