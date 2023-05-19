package com.example.mynotes.domain.use_cases.transaction_use_case

import javax.inject.Inject

class TransactionUseCases @Inject constructor(
    var add: TransactionAdd,
    var getByOwner: TransactionGetByOwner,
    var getForHome: TransactionGetForHome,
    var getAll: TransactionGetAll
)