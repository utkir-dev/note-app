package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Transaction
import com.example.data.db.models.History
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(transaction: Transaction): Long

    @Query("DELETE FROM transactions WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM transactions WHERE fromId=:ownerId or toId=:ownerId order by date desc")
    fun getByOwnerId(ownerId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions order by date desc limit:count")
    fun getForHome(count: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions order by date desc")
    fun getAll(): Flow<List<Transaction>>

    @Query(
        "SELECT transactions.type AS title, " +
                "transactions.amount AS amount,\n" +
                "currencies.name AS currency, \n" +
                "(SELECT name FROM persons WHERE id=transactions.fromId " +
                "UNION " +
                "SELECT name FROM pockets WHERE id=transactions.fromId) AS fromName,\n" +
                "(SELECT name FROM persons WHERE id=transactions.toId " +
                "UNION " +
                "SELECT name FROM pockets WHERE id=transactions.toId) as toName,\n" +
                "(SELECT ((1/currencies.rate)*transactions.amount*(SELECT rate FROM currencies WHERE id=transactions.currencyFrom limit 1)) +' '+ (SELECT name FROM currencies WHERE id=transactions.currencyFrom )) AS moneyFrom, \n" +
                "(SELECT ((1/currencies.rate)*transactions.amount*(SELECT rate FROM currencies WHERE id=transactions.currencyTo limit 1)) +' '+ (SELECT name FROM currencies WHERE id=transactions.currencyTo )) AS moneyTo, \n" +
                "transactions.comment AS comment, transactions.date AS date " +
                "FROM currencies, transactions where transactions.currencyId=currencies.id ORDER BY date DESC"
    )
    fun getHistory(): Flow<List<History>>
}
