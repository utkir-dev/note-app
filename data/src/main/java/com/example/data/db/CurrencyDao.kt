package com.example.data.db

import androidx.room.*
import com.example.common.ResponseResult
import com.example.data.entities.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(currency: Currency): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currency: Currency): Int

    @Delete
    suspend fun delete(currency: Currency): Int

    @Query("SELECT * FROM currencies WHERE name=:name")
    fun getCurrency(name: String): Flow<Currency>

    @Query("SELECT * FROM currencies")
    fun getAll(): Flow<List<Currency>>
}