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

    @Query("SELECT COUNT(*) FROM persons WHERE LOWER(name)=:name")
    fun getCount(name: String): Int

    @Query("DELETE FROM persons WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM persons")
    fun clear()

    @Query("SELECT * FROM persons WHERE id=:id limit 1")
    fun getPerson(id: String): Person

    @Query("SELECT * FROM persons order by date")
    fun getAll(): Flow<List<Person>>

    @Query("SELECT MAX(date) FROM persons")
    fun getLastUpdatedTime(): Long

    @Query("SELECT * FROM persons WHERE  date>:date order by date")
    fun getFromDate(date: Long): Flow<List<Person>>

    @Query("SELECT * FROM persons WHERE  uploaded=:uploaded")
    fun getNotUploaded(uploaded: Boolean): Flow<List<Person>>

}