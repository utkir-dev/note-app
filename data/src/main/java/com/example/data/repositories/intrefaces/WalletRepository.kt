package com.example.data.repositories.intrefaces

import com.example.data.db.entities.Wallet
import com.example.data.db.models.Balance
import com.example.data.db.models.WalletOwner
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun add(wallet: Wallet): Long
    suspend fun addWallets(wallets: List<Wallet>): List<Long>
    suspend fun update(wallet: Wallet): Int
    suspend fun delete(wallet: Wallet): Int
    suspend fun getByOwnerAndCurrencyId(ownerId: String, currencyId: String): Wallet
    suspend fun getByOwnerId(ownerId: String): Flow<List<Wallet>>
    suspend fun getAll(): Flow<List<Wallet>>
    suspend fun getBalabce(): Flow<List<Balance>>
    suspend fun getWalletsByOwnes(): Flow<List<WalletOwner>>
}