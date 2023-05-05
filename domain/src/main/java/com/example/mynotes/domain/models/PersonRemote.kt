package com.example.mynotes.domain.models

data class PersonRemote(
    val id: String,
    val name: String,
    val surname: String,
    var address: String,
    var date: Long
)
