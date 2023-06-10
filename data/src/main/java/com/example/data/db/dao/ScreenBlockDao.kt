package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Person
import com.example.data.db.entities.ScreenBlock
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface ScreenBlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(screenBlock: ScreenBlock): Long

    @Query("SELECT * FROM screen_block WHERE id=:id limit 1")
    fun getScreenBlock(id: String): Flow<ScreenBlock>

    @Query("DELETE FROM screen_block")
    fun clear()
}