package com.example.data.db.dao

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

    @Query("SELECT * FROM currencies WHERE id=:id limit 1")
    fun getById(id: String): Currency

    @Query("SELECT * FROM currencies WHERE id IN (:ids)")
    fun getCurrencies(ids: List<String>): Flow<List<Currency>>

    @Query("SELECT * FROM currencies order by date asc")
    fun getAll(): Flow<List<Currency>>


}