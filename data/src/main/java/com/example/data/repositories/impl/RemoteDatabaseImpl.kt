package com.example.data.repositories.impl

import com.example.data.repositories.intrefaces.RemoteDatabase
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

internal class RemoteDatabaseImpl @Inject constructor(
    private val remote: Firebase
) : RemoteDatabase {
    override val storageRef: Firebase
        get() = remote
}