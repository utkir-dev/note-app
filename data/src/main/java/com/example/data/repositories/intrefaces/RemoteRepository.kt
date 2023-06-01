package com.example.data.repositories.intrefaces

import com.example.common.UserDevice
import kotlinx.coroutines.flow.Flow


interface RemoteRepository {
    suspend fun uploadDataAsFile()
    suspend fun downloadAllData()
    suspend fun checkNotLoadedDatas()
    suspend fun observeDevice(): Flow<Boolean>
    suspend fun saveDevice()
    suspend fun getRemoteDevice(): UserDevice?
    suspend fun getLocalDeviceId(): String
}