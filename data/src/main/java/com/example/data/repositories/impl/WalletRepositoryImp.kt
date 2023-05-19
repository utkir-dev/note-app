package com.example.data.repositories.impl

import com.example.data.constants.Const.USERS
import com.example.data.constants.Const.WALLETS
import com.example.data.db.dao.WalletDao
import com.example.data.db.entities.Wallet
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.example.data.repositories.intrefaces.WalletRepository
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class WalletRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: WalletDao,
    private val auth: AuthRepository,

    ) : WalletRepository {
    override suspend fun add(wallet: Wallet): Long {
        val result = local.add(wallet)
        val dbRemote = remote.storageRef.firestore.collection(USERS)
            .document(auth.currentUser?.uid ?: "").collection(WALLETS)
        var remoteTask = false
        coroutineScope {
            val job1 = async {
                dbRemote.document(wallet.id).set(wallet.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }
            job1.await()
        }
        return result
    }

    override suspend fun update(wallet: Wallet): Int {
        val dbRemote = remote.storageRef.firestore.collection(USERS)
            .document(auth.currentUser?.uid ?: "").collection(WALLETS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(wallet.id).set(wallet.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }

            task1.await()
        }

        return local.update(wallet)
    }

    override suspend fun delete(wallet: Wallet): Int {
        val dbRemote = remote.storageRef.firestore.collection(USERS)
            .document(auth.currentUser?.uid ?: "").collection(WALLETS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(wallet.id)
                    .delete().addOnSuccessListener {
                        remoteTask = true
                    }
            }
            task1.await()

        }
        return local.delete(wallet.id)
    }

    override suspend fun getByOwnerAndCurrencyId(
        ownerId: String,
        currencyId: String
    ): Wallet {
        return local.getByCurrencyOwnerId(ownerId, currencyId)
    }

    override suspend fun getByOwnerId(ownerId: String): Flow<List<Wallet>> {
        return local.getByOwnerId(ownerId)
    }

    override suspend fun getAll(): Flow<List<Wallet>> {
        return local.getAll()
    }
}