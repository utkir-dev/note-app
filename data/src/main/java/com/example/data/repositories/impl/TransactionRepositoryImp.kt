package com.example.data.repositories.impl

import com.example.data.constants.Const.TRANSACTIONS
import com.example.data.constants.Const.USERS
import com.example.data.db.dao.TransactionDao
import com.example.data.db.entities.Transaction
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.example.data.repositories.intrefaces.TransactionRepository
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TransactionRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: TransactionDao,
    private val auth: AuthRepository
) : TransactionRepository {

    override suspend fun add(transaction: Transaction): Long {
        val result = local.add(transaction)
        val dbRemote = remote.storageRef.firestore
            .collection(USERS).document(auth.currentUser?.uid ?: "")
            .collection(TRANSACTIONS)
        var remoteTask = false
        coroutineScope {
            val job1 = async {
                dbRemote.document(transaction.id).set(transaction.toRemote())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            remoteTask = true

                        }
                    }
            }
            job1.await()
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

    override suspend fun getForHome(count: Int): Flow<List<Transaction>> {
        return local.getForHome(count)
    }

    override suspend fun getByOwnerId(id: String): Flow<List<Transaction>> {
        return local.getByOwnerId(id)
    }

    override suspend fun getAll() = local.getAll()
}