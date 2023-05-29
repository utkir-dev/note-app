package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Person
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(person: Person): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPersons(persons: List<Person>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(person: Person): Int

    @Query("DELETE FROM persons WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM persons WHERE id=:id limit 1")
    fun getPerson(id: String): Person

    @Query("SELECT * FROM persons order by date")
    fun getAll(): Flow<List<Person>>

}