package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.entities.Currency
import com.google.common.base.Converter

@Database(entities = [Currency::class], version = 1, exportSchema = true)
abstract class MyRoom : RoomDatabase() {

    abstract fun CurrencyDao(): CurrencyDao
    // abstract fun WalletDao(): WalletDao

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