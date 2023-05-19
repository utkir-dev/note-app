package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(transaction: Transaction): Long

    @Query("DELETE FROM transactions WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM transactions WHERE fromId=:ownerId or toId=:ownerId order by date desc")
    fun getByOwnerId(ownerId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions order by date desc limit:count")
    fun getForHome(count: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions order by date desc")
    fun getAll(): Flow<List<Transaction>>
}