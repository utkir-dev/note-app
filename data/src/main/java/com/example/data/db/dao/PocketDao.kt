package com.example.data.db.dao

import androidx.room.*
import com.example.common.ResponseResult
import com.example.data.db.entities.Currency
import com.example.data.db.entities.Pocket
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(pocket: Pocket): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(pocket: Pocket): Int

    @Query("DELETE FROM pockets WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM pockets WHERE id=:id limit 1")
    fun getPocket(id: String): Pocket

    @Query("SELECT * FROM pockets order by date")
    fun getAll(): Flow<List<Pocket>>
}