package com.example.data.db.dao

import androidx.room.*
import com.example.common.ResponseResult
import com.example.data.db.entities.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(currency: Currency): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrencies(currencies: List<Currency>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currency: Currency): Int

    @Query("DELETE FROM currencies WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM currencies")
    fun clear()

    @Query("SELECT COUNT(*) FROM currencies")
    fun getCount(): Int

    @Query("SELECT COUNT(*) FROM currencies WHERE LOWER(name)=:name")
    fun getCount(name: String): Int

    @Query("SELECT * FROM currencies WHERE id=:id LIMIT 1")
    fun getById(id: String): Currency

    @Query("SELECT * FROM currencies WHERE id IN (:ids)")
    fun getCurrencies(ids: List<String>): Flow<List<Currency>>

    @Query("SELECT MAX(date) FROM currencies")
    fun getLastUpdatedTime(): Long

    @Query("SELECT * FROM currencies order by name")
    fun getAll(): Flow<List<Currency>>

    @Query("SELECT * FROM currencies WHERE date>:date order by date asc")
    fun getFromDate(date: Long): Flow<List<Currency>>

    @Query("SELECT * FROM currencies WHERE uploaded=:uploaded")
    fun getNotUploaded(uploaded: Boolean): Flow<List<Currency>>

    @Query("SELECT COUNT(*) FROM currencies WHERE uploaded=:uploaded")
    fun getNotUploadedCount(uploaded: Boolean): Flow<Int>

}