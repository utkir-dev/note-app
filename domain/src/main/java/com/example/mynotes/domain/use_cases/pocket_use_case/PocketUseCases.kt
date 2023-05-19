package com.example.mynotes.domain.use_cases.pocket_use_case

import javax.inject.Inject

class PocketUseCases @Inject constructor(
    var add: PocketAdd,
    var delete: PocketDelete,
    var getOne: PocketGet,
    var getAll: PocketGetAll
)