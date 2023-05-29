package com.example.mynotes.presentation.ui.screens.main.pocket_info

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import kotlinx.coroutines.flow.Flow


interface PocketViewModel {
    val pocket: MutableState<PocketDomain>
    val walletsByOwners: Flow<List<WalletOwnerDomain>>
    fun setPocket(pocket: PocketDomain)
    fun back()
}