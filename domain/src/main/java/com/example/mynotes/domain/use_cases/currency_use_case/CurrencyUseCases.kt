package com.example.mynotes.domain.use_cases.currency_use_case

import javax.inject.Inject

class CurrencyUseCases @Inject constructor(
    var add: CurrencyAdd,
    var update: CurrencyUpdate,
    var delete: CurrencyDelete,
    var getAll: CurrencyGetAll,
    var getCount: CurrencyGetCount,
)