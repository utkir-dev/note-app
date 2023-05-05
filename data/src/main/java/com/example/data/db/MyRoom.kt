package com.example.mynotes.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.data.db.dao.CurrencyDao
import com.example.mynotes.data.db.dao.WalletDao
import com.example.mynotes.data.entities.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class MyRoom : RoomDatabase() {

    abstract fun CurrencyDao(): CurrencyDao
    abstract fun WalletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANSE: MyRoom? = null
        val DATABASE_NAME = "my_note_db"
        fun getInstance(ctx: Context): MyRoom {
            return INSTANSE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    MyRoom::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANSE = instance
                instance
            }
        }
    }
}