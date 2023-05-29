package com.example.mynotes.domain.models


data class PersonWithWalletsDomain(
    val person: PersonDomain,
    val wallets: List<WalletDomain>
)