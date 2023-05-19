package com.example.mynotes.domain.use_cases.wallet_use_case

import javax.inject.Inject

class WalletUseCases @Inject constructor(
    var add: WalletAdd,
    var delete: WalletDelete,
    var getByOwnerId: WalletGetByOwnerId,
    var getAll: WalletGetAll
)