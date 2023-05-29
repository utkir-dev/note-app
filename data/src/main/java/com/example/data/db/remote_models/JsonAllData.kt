package com.example.data.db.remote_models

import com.example.data.db.entities.Device


data class AllData(
    var date: Long = 0,
    var currency: List<CurrencyRemote> = emptyList(),
    var pocket: List<PocketRemote> = emptyList(),
    var person: List<PersonRemote> = emptyList(),
    var wallet: List<WalletRemote> = emptyList(),
    var transaction: List<TransactionRemote> = emptyList(),
    var device: List<Device> = emptyList(),
)
