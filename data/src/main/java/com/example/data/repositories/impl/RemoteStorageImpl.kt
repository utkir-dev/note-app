package com.example.data.repositories.impl

import com.example.data.repositories.intrefaces.RemoteStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

internal class RemoteStorageImpl @Inject constructor(
    private val remote: StorageReference
) : RemoteStorage {
    override val storage: StorageReference
        get() = remote
}