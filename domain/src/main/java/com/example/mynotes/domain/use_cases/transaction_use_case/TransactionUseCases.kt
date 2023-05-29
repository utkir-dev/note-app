package com.example.mynotes.domain.use_cases.transaction_use_case

import javax.inject.Inject

class TransactionUseCases @Inject constructor(
    var add: TransactionIncomeOutcome,
    var getByOwner: TransactionGetByOwner,
    var getAll: TransactionGetAll,
    var getHistory: TransactionGetHistory,
    var getHistoryForHome: TransactionGetForHome,
    var getHistoryById: TransactionsByOwnerId,
    var getConvertation: TransactionConvertation,
)