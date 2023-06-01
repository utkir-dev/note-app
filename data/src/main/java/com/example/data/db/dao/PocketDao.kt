package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Pocket
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(pocket: Pocket): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPockets(pockets: List<Pocket>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(pocket: Pocket): Int

    @Query("SELECT COUNT(*) FROM pockets WHERE LOWER(name)=:name")
    fun getCount(name: String): Int

    @Query("SELECT COUNT(*) FROM pockets")
    fun getCount(): Int

    @Query("DELETE FROM pockets WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM pockets")
    fun clear()

    @Query("SELECT * FROM pockets WHERE id=:id limit 1")
    fun getPocket(id: String): Pocket

    @Query("SELECT * FROM pockets order by date")
    fun getAll(): Flow<List<Pocket>>

    @Query("SELECT MAX(date) FROM pockets")
    fun getLastUpdatedTime(): Long

    @Query("SELECT * FROM pockets WHERE date>:date order by date")
    fun getFromDate(date: Long): Flow<List<Pocket>>

    @Query("SELECT * FROM pockets WHERE uploaded=:uploaded")
    fun getNotUploaded(uploaded: Boolean): Flow<List<Pocket>>

}