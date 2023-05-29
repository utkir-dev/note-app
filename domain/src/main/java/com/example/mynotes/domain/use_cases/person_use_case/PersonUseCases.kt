package com.example.mynotes.domain.use_cases.person_use_case

import javax.inject.Inject

class PersonUseCases @Inject constructor(
    var add: PersonAdd,
    var delete: PersonDelete,
    var update: PersonUpdate,
    var getOne: PersonGet,
    var getAll: PersonGetAll,
)