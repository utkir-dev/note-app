package com.example.data.repositories.intrefaces

import com.example.common.UserDevice
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


interface RemoteRepository {
    suspend fun uploadDataAsFile(doNow: Boolean?)
    suspend fun checkNotLoadedDatas()
    suspend fun observeDevice(): Flow<Boolean>
    suspend fun stopObserveDevice()
    suspend fun saveDevice()
    suspend fun getRemoteDevice(): UserDevice?
    suspend fun getLocalDeviceId(): String
    suspend fun downloadAllData(): Flow<Boolean>
}