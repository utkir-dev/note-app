package com.example.mynotes.domain.background_task

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.common.UserDevice
import com.example.data.constants.Const
import com.example.data.repositories.intrefaces.AuthRepository
import com.example.data.repositories.intrefaces.RemoteRepository
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.device_use_case.DeviceUseCases
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


@HiltWorker
class CheckDatabase @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val deviceUseCases: DeviceUseCases,
    private val auth: AuthRepository,
    private val remoteStorage: RemoteRepository,
    private val dataUseCases: DataUseCases,
) : CoroutineWorker(
    appContext,
    params
) {
    override suspend fun doWork(): Result {
        coroutineScope {
            if (auth.currentUser != null) {
                FirebaseFirestore.getInstance().collection(Const.USERS)
                    .document(auth.currentUser?.uid ?: "")
                    .collection(Const.DEVICES).document(Const.DEVICES).get()
                    .addOnSuccessListener {
                        val remoteDevice = it.toObject(UserDevice::class.java)
                        runBlocking {
                            val localDeviceId: String =
                                deviceUseCases.getLocalDeviceId.invoke()
                            remoteDevice?.let {
                                if (it.id != localDeviceId) {
                                    auth.signOut()
                                    dataUseCases.clearDbLocal.invoke()
                                }
                            }
                        }
                    }.await()
                delay(10_000)
                async { remoteStorage.checkNotLoadedDatas() }.await()
                async { remoteStorage.uploadDataAsFile(false) }.await()
            }
        }
        return Result.success()
    }
}
