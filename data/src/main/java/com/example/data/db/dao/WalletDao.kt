package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(wallet: Wallet): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(wallet: Wallet): Int

    @Query("DELETE FROM wallets WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId and currencyId=:currencyId")
    suspend fun getByCurrencyOwnerId(ownerId: String, currencyId: String): Wallet

    @Query("SELECT * FROM wallets WHERE currencyId=:currencyId order by date asc")
    fun getByCurrencyId(currencyId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId order by date asc")
    fun getByOwnerId(ownerId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets order by date asc")
    fun getAll(): Flow<List<Wallet>>
}