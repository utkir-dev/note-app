package com.example.data.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.db.entities.*


@Database(
    entities = [
        Currency::class,
        Pocket::class,
        Person::class,
        Wallet::class,
        Transaction::class
    ],
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

        //        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "DROP TABLE IF EXISTS `screen_block`"
//                )
//
//                database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS `screen_block` (`id` TEXT, `pincode` TEXT, `access` INTEGER, `date` INTEGER, " +
//                            "PRIMARY KEY(`id`))"
//                )
//            }
//        }
//        val MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "DROP TABLE IF EXISTS `screen_block`"
//                )
//                database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS `screen_block` (`id` TEXT DEFAULT 'id_block_screen' NOT NULL, `pincode` TEXT DEFAULT '1234' NOT NULL, `access` INTEGER  DEFAULT '0'  NOT NULL, `date` INTEGER DEFAULT '0'  NOT NULL, " +
//                            "PRIMARY KEY(`id`))"
//                )
//            }
//        }
        fun getInstance(ctx: Context): MyRoom {
            return INSTANSE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    MyRoom::class.java,
                    DATABASE_NAME
                )
                    //.addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // default valyuta

                            val values = ContentValues()
                            values.put("id", "dollar")
                            values.put("name", "dollar")
                            values.put("rate", 1.0)
                            values.put("date", System.currentTimeMillis())
                            values.put("uploaded", false)
                            db.insert("currencies", SQLiteDatabase.CONFLICT_IGNORE, values)

                        }
                    })

                    .build()
                INSTANSE = instance
                instance
            }

        }
    }
}
