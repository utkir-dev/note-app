package com.example.data.repositories.impl

import com.example.data.constants.Const.TRANSACTIONS
import com.example.data.constants.Const.USERS
import com.example.data.db.dao.TransactionDao
import com.example.data.db.entities.Transaction
import com.example.data.db.entities.database_relations.History
import com.example.data.db.remote_models.TransactionRemote
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.example.data.repositories.intrefaces.TransactionRepository
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class TransactionRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: TransactionDao,
    // private val remoteStorage: RemoteRepository,
    private val auth: AuthRepository
) : TransactionRepository {

    override suspend fun add(transaction: Transaction): Long {
        val result = local.add(transaction)
        val dbRemote = remote.storageRef.firestore
            .collection(USERS).document(auth.currentUser?.uid ?: "")
            .collection(TRANSACTIONS)

        dbRemote.document(transaction.id)
            .set(transaction.toRemote())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    runBlocking {
                        local.add(
                            transaction.copy(
                                uploaded = true
                            )
                        )
                    }
                }
            }
        return result

    }

    override suspend fun delete(transaction: Transaction): Int {
        val dbRemote =
            remote.storageRef.firestore.collection(USERS).document(auth.currentUser?.uid ?: "")
                .collection(TRANSACTIONS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(transaction.id)
                    .delete().addOnSuccessListener {
                        remoteTask = true
                    }
            }
            task1.await()

        }
        return local.delete(transaction.id)
    }

    override suspend fun getForHome(count: Int): Flow<List<History>> {
        return local.getForHome(count)
    }

    override suspend fun download(count: Int) {
        val lastDate = local.getLastDate()
        val remote = remote.storageRef.firestore.collection(
            USERS
        ).document(auth.currentUser?.uid ?: "").collection(TRANSACTIONS)
        remote.orderBy("date")
            .endBefore(lastDate)
            .limitToLast(count.toLong())
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) try {
                    val list = snapshot.map {
                        it.toObject(
                            TransactionRemote::class.java
                        )
                    }
                    runBlocking {
                        local.addTransactions(list.map { it.toLocal() })
                    }
                } catch (_: Exception) {
                }
            }
    }

    override suspend fun getByOwnerId(ownerId: String): Flow<List<History>> {
        return local.getByOwnerId(ownerId)
    }

    override suspend fun getAll() = local.getAll()
    override suspend fun getHistory(): Flow<List<History>> {
        return local.getHistory()
    }

    override suspend fun getHistory(limit: Int, page: Int): Flow<List<History>> {
        return local.getHistory(limit, page)
    }

    override suspend fun getHistoryCount(): Flow<Int> {
        return local.getHistoryCount()
    }

}