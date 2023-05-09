package com.example.mynotes.domain.models

data class PocketDomain(
    override val id: String,
    val personId: String,
    override var name: String,
    var date: Long
) : ModelDomain
