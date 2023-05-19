package com.example.data.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.db.entities.Currency
import com.example.data.db.entities.Pocket
import com.example.data.db.entities.Transaction
import com.example.data.db.entities.Wallet

@Database(
    entities = [Currency::class, Pocket::class, Wallet::class, Transaction::class],
    version = 1,
    exportSchema = true
)
abstract class MyRoom : RoomDatabase() {

    abstract fun CurrencyDao(): CurrencyDao
    abstract fun PocketDao(): PocketDao
    abstract fun WalletDao(): WalletDao
    abstract fun TransactionDao(): TransactionDao

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
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANSE = instance
                instance
            }
        }
    }
}