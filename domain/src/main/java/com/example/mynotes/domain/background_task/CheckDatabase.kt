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
        Log.d("worker", "doWork")

        coroutineScope {
            if (auth.currentUser != null) {
                Log.d("worker", "runBlocking")
                FirebaseFirestore.getInstance().collection(Const.USERS)
                    .document(auth.currentUser?.uid ?: "")
                    .collection(Const.DEVICES).document(Const.DEVICES).get()
                    .addOnSuccessListener {
                        val remoteDevice = it.toObject(UserDevice::class.java)
                        Log.d("worker", "remoteDevice : $remoteDevice")
                        runBlocking {
                            val localDeviceId: String =
                                deviceUseCases.getLocalDeviceId.invoke()
                            Log.d("worker", "localDeviceId : $localDeviceId")
                            remoteDevice?.let {
                                if (it.id != localDeviceId) {
                                    auth.signOut()
                                    dataUseCases.clearDbLocal.invoke()
                                }
                            }
                        }
                    }.await()

                delay(10_000)
                Log.d("worker", "runBlocking end")
                async { remoteStorage.checkNotLoadedDatas() }
                Log.d("worker", "checkNotLoadedDatas end")
                async { remoteStorage.uploadDataAsFile() }
                Log.d("worker", "uploadDataAsFile end")
            }
        }
        return Result.success()
    }
}
