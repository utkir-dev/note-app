package com.example.data.repositories.impl

import com.example.data.constants.Const
import com.example.data.constants.Const.PERSONS
import com.example.data.db.dao.PersonDao
import com.example.data.db.entities.Person
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.PersonRepository
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PersonRepositoryImp @Inject constructor(
    private val remote: RemoteDatabase,
    private val local: PersonDao,
    private val auth: AuthRepository,
) : PersonRepository {

    override suspend fun add(person: Person): Long {
        val result = local.add(person)
        val dbRemote = remote.storageRef.firestore.collection(Const.USERS)
            .document(auth.currentUser?.uid ?: "").collection(PERSONS)
        var remoteTask = false

        coroutineScope {
            val job1 = async {
                dbRemote.document(person.id).set(person.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true

                    }
                }
            }
            job1.await()
        }
        return result
    }

    override suspend fun update(person: Person): Int {
        val dbRemote = remote.storageRef.firestore.collection(Const.USERS)
            .document(auth.currentUser?.uid ?: "").collection(PERSONS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(person.id).set(person.toRemote()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        remoteTask = true
                    }
                }
            }
            task1.await()
        }

        return local.update(person)
    }

    override suspend fun delete(person: Person): Int {
        val dbRemote = remote.storageRef.firestore.collection(Const.USERS)
            .document(auth.currentUser?.uid ?: "").collection(PERSONS)
        var remoteTask = false
        coroutineScope {
            val task1 = async {
                dbRemote.document(person.id)
                    .delete().addOnSuccessListener {
                        remoteTask = true
                    }
            }
            task1.await()

        }
        return local.delete(person.id)
    }


    override suspend fun get(name: String): Person {
        return local.getPerson(name)
    }

    override suspend fun getAll(): Flow<List<Person>> {
        return local.getAll()
    }


}