package com.example.mynotes.presentation.ui.screens.main.pockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
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

    override fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            pocketUseCases.delete.invoke(getPocket())
        }
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

    override var wallets: Flow<List<WalletDomain>> = flow {
        emitAll(walletUseCases.getAll.invoke())
    }
    val pocket = MutableStateFlow(PocketDomain(""))
    fun getPocket() = pocket.value
    fun savePocket(name: String) {
        if (pocket.value.name != name) {
            val pocketNew = if (pocket.value.isValid()) {
                pocket.value.copy(
                    name = name,
                    date = System.currentTimeMillis(),
                    uploaded = false
                )
            } else PocketDomain(
                id = UUID.randomUUID().toString(),
                name = name,
                date = System.currentTimeMillis()
            )
            add(pocketNew)
        }
    }
}