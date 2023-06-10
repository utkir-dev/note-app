package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Wallet
import com.example.data.db.entities.database_relations.Balance
import com.example.data.db.entities.database_relations.WalletOwner
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(wallet: Wallet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWallets(wallets: List<Wallet>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(wallet: Wallet): Int

    @Query("DELETE FROM wallets WHERE id=:id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM wallets")
    fun clear()

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId and currencyId=:currencyId")
    suspend fun getByCurrencyOwnerId(ownerId: String, currencyId: String): Wallet

    @Query("SELECT * FROM wallets WHERE currencyId=:currencyId order by date asc")
    fun getByCurrencyId(currencyId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId order by date asc")
    fun getByOwnerId(ownerId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets order by date asc")
    fun getAll(): Flow<List<Wallet>>

    @Query("SELECT MAX(date) FROM wallets")
    fun getLastUpdatedTime(): Long

    @Query("SELECT * FROM wallets WHERE date>:date order by date asc")
    fun getFromDate(date: Long): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE uploaded=:uploaded")
    fun getNotUploaded(uploaded: Boolean): Flow<List<Wallet>>

    @Query(
        "SELECT currencies.name as name,\n" +
                "SUM(wallets.balance) as amount,\n" +
                "currencies.rate as rate \n" +
                "FROM wallets,currencies \n" +
                "WHERE wallets.currencyId=currencies.id \n" +
                "AND wallets.ownerId NOT IN (SELECT id FROM persons) " +
                "GROUP BY currencies.id \n" +
                "ORDER BY currencies.name"
    )
    fun getBalances(): Flow<List<Balance>>

    @Query(
        "SELECT wallets.id as id, wallets.ownerId as ownerId, currencies.name as currencyName,\n" +
                "wallets.balance as currencyBalance, \n" +
                "currencies.rate as rate \n" +
                "FROM wallets,currencies \n" +
                "WHERE wallets.currencyId=currencies.id \n" +
                "GROUP BY wallets.id"
    )
    fun getWalletsByOwnerGroup(): Flow<List<WalletOwner>>

    @Query(
        "SELECT wallets.id as id, wallets.ownerId as ownerId, currencies.name as currencyName,\n" +
                "wallets.balance as currencyBalance, \n" +
                "currencies.rate as rate \n" +
                "FROM wallets,currencies \n" +
                "WHERE wallets.currencyId=currencies.id and wallets.ownerId=:id\n" +
                "GROUP BY wallets.id"
    )
    fun getWalletsByOwnerId(id: String): Flow<List<WalletOwner>>
}

