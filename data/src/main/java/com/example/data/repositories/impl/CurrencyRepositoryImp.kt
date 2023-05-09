package com.example.data.repositories.impl

import android.util.Log
import com.example.data.constants.Const.CURRENCIES
import com.example.data.db.CurrencyDao
import com.example.data.db.entities.Currency
import com.example.data.repositories.CurrencyRepository
import com.example.data.repositories.RemoteDatabase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: CurrencyDao
) : CurrencyRepository {

    override suspend fun add(currency: Currency): Long {
        var result = local.add(currency)
        val dbRemote = remote.storageRef.firestore.collection(CURRENCIES)
        var remoteTask = false
        Log.d("aaa", "remoteTask1 = $remoteTask")

        coroutineScope {
            val job1 = async {
                dbRemote.document(currency.id).set(currency.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true

                    }
                }.await()
                local.add(currency.copy(uploaded = true))
                Log.d("aaa", "remoteTask2 = $remoteTask")
            }
            return@coroutineScope
        }
        return result
    }

    override suspend fun update(currency: Currency): Int {
        val dbRemote = remote.storageRef.firestore.collection(CURRENCIES)
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
        val dbRemote = remote.storageRef.firestore.collection(CURRENCIES)
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


    override suspend fun get(name: String): Flow<Currency> {
        return local.getCurrency(name)
    }

    override suspend fun getAll(): Flow<List<Currency>> {
        return local.getAll()
    }
}