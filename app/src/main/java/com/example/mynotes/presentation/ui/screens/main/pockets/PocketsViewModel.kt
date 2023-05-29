package com.example.mynotes.presentation.ui.screens.main.pockets

import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import kotlinx.coroutines.flow.Flow


interface PocketsViewModel {
    val pockets: Flow<List<PocketDomain>>
    val walletsByOwners: Flow<List<WalletOwnerDomain>>

    fun add(pocket: PocketDomain)
    fun update(pocket: PocketDomain)
    fun delete(pocket: PocketDomain)
    fun back()
    fun navigateToPocket(pocket: PocketDomain)
}