package com.example.data.repositories.impl

import com.example.data.constants.Const.CURRENCIES
import com.example.data.db.CurrencyDao
import com.example.data.entities.Currency
import com.example.data.repositories.CurrencyRepository
import com.example.data.repositories.RemoteDatabase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class CurrencyRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: CurrencyDao
) : CurrencyRepository {

    override suspend fun add(name: String, rate: Double): Long {
        val currency = Currency(
            id = UUID.randomUUID().toString(),
            name = name,
            rate = rate,
            date = System.currentTimeMillis()
        )

        val dbRemote = remote.storageRef.firestore.collection(CURRENCIES)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(currency.id).set(currency).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }
            task1.await()
        }

        return local.add(currency)
    }

    override suspend fun update(currency: Currency): Int {
        val dbRemote = remote.storageRef.firestore.collection(CURRENCIES)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(currency.id).set(currency).addOnCompleteListener {
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
        return local.delete(currency)

    }


    override suspend fun get(name: String): Flow<Currency> {
        return local.getCurrency(name)
    }

    override suspend fun getAll(): Flow<List<Currency>> {
        return local.getAll()
    }
}