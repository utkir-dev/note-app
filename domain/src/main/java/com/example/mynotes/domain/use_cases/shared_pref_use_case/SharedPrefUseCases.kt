package com.example.mynotes.domain.use_cases.shared_pref_use_case

import javax.inject.Inject

class SharedPrefUseCases @Inject constructor(
    var saveBoolen: SaveBooleanUseCase,
    var getBoolean: GetBooleanUseCase,
)