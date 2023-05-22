package com.example.mynotes.domain.use_cases.currency_use_case

import javax.inject.Inject

class CurrencyUseCases @Inject constructor(
    var add: CurrencyAdd,
    var delete: CurrencyDelete,
    var getByWalletIds: CurrencyGetByWalletIds,
    var getAll: CurrencyGetAll,
    var getById: CurrencyGetById
)