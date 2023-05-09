package com.example.mynotes.domain.models

data class PersonDomain(
    override val id: String,
    override var name: String,
    var surname: String,
    var address: String,
    var date: Long
) : ModelDomain
