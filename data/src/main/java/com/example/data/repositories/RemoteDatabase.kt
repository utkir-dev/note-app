package com.example.data.repositories

import com.google.firebase.ktx.Firebase

interface RemoteDatabase {
    val storageRef: Firebase
}