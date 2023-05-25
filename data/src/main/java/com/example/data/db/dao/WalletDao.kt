package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.Wallet
import com.example.data.db.models.Balance
import com.example.data.db.models.WalletOwner
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

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId and currencyId=:currencyId")
    suspend fun getByCurrencyOwnerId(ownerId: String, currencyId: String): Wallet

    @Query("SELECT * FROM wallets WHERE currencyId=:currencyId order by date asc")
    fun getByCurrencyId(currencyId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE ownerId=:ownerId order by date asc")
    fun getByOwnerId(ownerId: String): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets order by date asc")
    fun getAll(): Flow<List<Wallet>>

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

}

