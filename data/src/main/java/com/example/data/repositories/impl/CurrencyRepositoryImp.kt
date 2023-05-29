package com.example.data.repositories.impl

import com.example.data.constants.Const.CURRENCIES
import com.example.data.constants.Const.USERS
import com.example.data.db.dao.CurrencyDao
import com.example.data.db.entities.Currency
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.CurrencyRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CurrencyRepositoryImp @Inject constructor(

    private val remote: RemoteDatabase,
    private val local: CurrencyDao,
    private val auth: AuthRepository

) : CurrencyRepository {

    override suspend fun add(currency: Currency): Long {
        val result = local.add(currency)
        val dbRemote = remote.storageRef.firestore
            .collection(USERS).document(auth.currentUser?.uid ?: "")
            .collection(CURRENCIES)
        var remoteTask = false
        coroutineScope {
            val job1 = async {
                dbRemote.document(currency.id).set(currency.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true

                    }
                }
            }
            job1.await()
        }
        return result
    }

    override suspend fun update(currency: Currency): Int {
        val dbRemote = remote.storageRef.firestore
            .collection(USERS).document(auth.currentUser?.uid ?: "")
            .collection(CURRENCIES)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(currency.id).set(currency.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }

            task1.await()
        }

        return local.update(currency)
    }

    override suspend fun delete(currency: Currency): Int {
        val dbRemote =
            remote.storageRef.firestore.collection(USERS).document(auth.currentUser?.uid ?: "")
                .collection(CURRENCIES)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(currency.id)
                    .delete().addOnSuccessListener {
                        remoteTask = true
                    }
            }
            task1.await()

        }
        return local.delete(currency.id)

    }

    override suspend fun getById(id: String): Currency {
        return local.getById(id)
    }


    override suspend fun getByWalletIds(ids: List<String>): Flow<List<Currency>> {
        return local.getCurrencies(ids)
    }

    override suspend fun getAll() = local.getAll()
    override suspend fun getCount() = local.getCount()
}