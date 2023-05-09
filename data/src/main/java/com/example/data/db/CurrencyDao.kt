package com.example.data.db

import androidx.room.*
import com.example.common.ResponseResult
import com.example.data.db.entities.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(currency: Currency): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currency: Currency): Int

    @Query("DELETE FROM currencies WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM currencies WHERE name=:name")
    fun getCurrency(name: String): Flow<Currency>

    @Query("SELECT * FROM currencies")
    fun getAll(): Flow<List<Currency>>
}