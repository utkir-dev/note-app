package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Transaction
import com.example.data.db.entities.database_relations.History
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(transaction: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransactions(transactions: List<Transaction>): List<Long>

    @Query("DELETE FROM transactions WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM transactions")
    fun clear()

    @Query("SELECT * FROM transactions WHERE id=:id LIMIT 1")
    fun getById(id: String): Transaction

    @Query("SELECT * FROM transactions order by date desc")
    fun getAll(): Flow<List<Transaction>>


    @Query("SELECT COUNT(*) FROM transactions")
    fun getCount(): Int

    @Query("SELECT MAX(date) FROM transactions")
    fun getLastUpdatedTime(): Long

    @Query("SELECT * FROM transactions WHERE date>:date order by date desc")
    fun getFromDate(date: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE uploaded=:uploaded")
    fun getNotUploaded(uploaded: Boolean): Flow<List<Transaction>>

    @Query("SELECT COUNT(*) FROM transactions WHERE uploaded=:uploaded")
    fun getNotUploadedCount(uploaded: Boolean): Flow<Int>

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
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateFrom) AS moneyFrom, \n" +
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateTo) AS moneyTo, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyFrom limit 1) AS moneyNameFrom, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyTo limit 1) AS moneyNameTo, \n" +
                "transactions.comment AS comment, transactions.date AS date, \n " +
                "transactions.isFromPocket AS isFromPocket, transactions.isToPocket AS isToPocket, \n" +
                "transactions.rate AS rate, transactions.rateFrom AS rateFrom, \n" +
                "transactions.rateTo AS rateTo, transactions.balance AS balance, \n" +
                "transactions.id AS transactionId, transactions.uploaded AS uploaded \n" +
                "FROM currencies, transactions where transactions.currencyId=currencies.id AND (transactions.fromId=:ownerId " +
                "OR transactions.toId=:ownerId) ORDER BY date DESC"
    )
    fun getByOwnerId(ownerId: String): Flow<List<History>>

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
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateFrom) AS moneyFrom, \n" +
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateTo) AS moneyTo, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyFrom limit 1) AS moneyNameFrom, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyTo limit 1) AS moneyNameTo, \n" +
                "transactions.comment AS comment, transactions.date AS date, \n " +
                "transactions.isFromPocket AS isFromPocket, transactions.isToPocket AS isToPocket, \n" +
                "transactions.rate AS rate, transactions.rateFrom AS rateFrom, \n" +
                "transactions.rateTo AS rateTo, transactions.balance AS balance, \n" +
                "transactions.id AS transactionId, transactions.uploaded AS uploaded \n" +
                "FROM currencies, transactions where transactions.currencyId=currencies.id ORDER BY date DESC limit :count"
    )
    fun getForHome(count: Int): Flow<List<History>>


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
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateFrom) AS moneyFrom, \n" +
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateTo) AS moneyTo, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyFrom limit 1) AS moneyNameFrom, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyTo limit 1) AS moneyNameTo, \n" +
                "transactions.comment AS comment, transactions.date AS date, \n " +
                "transactions.isFromPocket AS isFromPocket, transactions.isToPocket AS isToPocket, \n" +
                "transactions.rate AS rate, transactions.rateFrom AS rateFrom, \n" +
                "transactions.rateTo AS rateTo, transactions.balance AS balance, \n" +
                "transactions.id AS transactionId, transactions.uploaded AS uploaded \n" +
                "FROM currencies, transactions where transactions.currencyId=currencies.id ORDER BY date DESC"
    )
    fun getHistory(): Flow<List<History>>

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
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateFrom) AS moneyFrom, \n" +
                "(SELECT (1/transactions.rate)*transactions.amount*transactions.rateTo) AS moneyTo, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyFrom limit 1) AS moneyNameFrom, \n" +
                "(SELECT name FROM currencies WHERE currencies.id=transactions.currencyTo limit 1) AS moneyNameTo, \n" +
                "transactions.comment AS comment, transactions.date AS date, \n " +
                "transactions.isFromPocket AS isFromPocket, transactions.isToPocket AS isToPocket, \n" +
                "transactions.rate AS rate, transactions.rateFrom AS rateFrom, \n" +
                "transactions.rateTo AS rateTo, transactions.balance AS balance, \n" +
                "transactions.id AS transactionId, transactions.uploaded AS uploaded \n" +
                "FROM currencies, transactions where transactions.currencyId=currencies.id ORDER BY date DESC " +
                "LIMIT :limit OFFSET :page"
    )
    fun getHistory(limit: Int, page: Int): Flow<List<History>>
}

