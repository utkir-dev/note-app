package com.example.data.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.db.entities.*


@Database(
    entities = [Currency::class, Pocket::class, Person::class, Wallet::class, Transaction::class],
    version = 1,
    exportSchema = true
)
abstract class MyRoom : RoomDatabase() {

    abstract fun CurrencyDao(): CurrencyDao
    abstract fun PocketDao(): PocketDao
    abstract fun PersonDao(): PersonDao
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
                    .fallbackToDestructiveMigration().addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // default valyuta

                            val values = ContentValues()
                            values.put("id", "dollar")
                            values.put("name", "dollar")
                            values.put("rate", 1.0)
                            values.put("date", System.currentTimeMillis())
                            values.put("uploaded", false)
                            db.insert("currencies", SQLiteDatabase.CONFLICT_REPLACE, values)
                        }
                    })
                    .build()
                INSTANSE = instance
                instance
            }

        }
    }
}
