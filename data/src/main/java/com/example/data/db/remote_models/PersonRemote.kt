package com.example.data.db.remote_models

data class PersonRemote(
    val id: String,
    val name: String,
    val surname: String,
    var address: String,
    var date: Long
)
