package com.example.data.repositories.intrefaces


interface RemoteRepository {
    suspend fun upload()
    suspend fun download()
}