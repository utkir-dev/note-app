package com.example.mynotes.presentation.ui.screens.main.pockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PocketsViewModelImp @Inject constructor(
    private val pocketUseCases: PocketUseCases,
    private val walletUseCases: WalletUseCases,
    private val direction: PocketsDirection
) : ViewModel(), PocketsViewModel {

    override val walletsByOwners: Flow<List<WalletOwnerDomain>> = flow {
        emitAll(walletUseCases.getWalletsByOwnes.invoke())
    }
    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(pocketUseCases.getAll.invoke())
    }

    override fun add(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            pocketUseCases.add.invoke(pocket)
        }
    }

    override fun update(pocket: PocketDomain) {

    }

    override fun delete(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.IO) { pocketUseCases.delete.invoke(pocket) }

    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    override fun navigateToPocket(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.Default) {
            direction.navigateToPocket(pocket)
        }
    }
}