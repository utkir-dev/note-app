package com.example.data.repositories.impl

import com.example.data.constants.Const
import com.example.data.constants.Const.POCKETS
import com.example.data.db.dao.PocketDao
import com.example.data.db.entities.Pocket
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.PocketRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class PocketRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: PocketDao,
    private val auth: AuthRepository,
) : PocketRepository {

    override suspend fun add(pocket: Pocket): Long {
        val result = local.add(pocket)
        val dbRemote = remote.storageRef.firestore
            .collection(Const.USERS).document(auth.currentUser?.uid ?: "")
            .collection(POCKETS)

        dbRemote.document(pocket.id).set(pocket.toRemote().copy(date = System.currentTimeMillis()))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    runBlocking {
                        local.add(pocket.copy(date = System.currentTimeMillis(), uploaded = true))
                    }
                }
            }
        return result
    }

    override suspend fun update(pocket: Pocket): Int {
        val dbRemote = remote.storageRef.firestore.collection(Const.USERS)
            .document(auth.currentUser?.uid ?: "").collection(POCKETS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(pocket.id).set(pocket.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }
            task1.await()
        }

        return local.update(pocket)
    }

    override suspend fun delete(pocket: Pocket): Int {
        val dbRemote = remote.storageRef.firestore.collection(Const.USERS)
            .document(auth.currentUser?.uid ?: "").collection(POCKETS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(pocket.id)
                    .delete().addOnSuccessListener {
                        remoteTask = true
                    }
            }
            task1.await()

        }
        return local.delete(pocket.id)
    }


    override suspend fun get(name: String): Pocket {
        return local.getPocket(name)
    }

    override suspend fun getAll(): Flow<List<Pocket>> {
        return local.getAll()
    }
}